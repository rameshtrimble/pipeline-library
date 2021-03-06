def call(String unit = "MILLISECONDS") {

    def divisor = ["HOURS": 360000, "MINUTES": 60000, "SECONDS": 1000 , "MILLISECONDS": 1]
    long completedTimeStamp = currentBuild.getTimeInMillis()
    long prevTimeStamp = getTimeOfFailedBuild(currentBuild)
    recoveryTime = completedTimeStamp - prevTimeStamp
    echo "last failed build was: ${recoveryTime} ago "
    sendBuildEvent(eventType:'state-change', state: 'unhealthy', priorDuration: recoveryTime  )
    return recoveryTime / divisor[unit]
}

@NonCPS
long getTimeOfFailedBuild(currentBuild) {
    def build = currentBuild.getPreviousBuild() //start looking at previous build
    while(build.getNumber() > 1 && build.getResult() != 'FAILURE') {
        build = build.getPreviousBuild()
    }
    println "Last failed build was ${build.getNumber()}"
    return build.getTimeInMillis()
}