<h3>
Intro
</h3>
Youtube sniffer is a sample project to prove some skills in automation =D
<br>
<br>
The fun thing that this stuff is capable of:

- searching for N youtube videos by some query and check whether adblock plugin helps to avoid preroll ad or not.
- produces reports for run with adblock on and off in format of pipe table |videoId|adAppearance|
<br>
<h3>
Operation
</h3>
Building is simple:

- run mvn install
- get youtube-ad-sniffer-*-jar-with-dependencies.jar from target folder or local m2 repo
- create *.txt properties file with:
    - webdriver.driver= (i.e. chrome. The only browser that is supported for now)
    - webdriver.chrome.driver= (path to chromedriver executable. Can be got from https://sites.google.com/a/chromium.org/chromedriver/downloads)
    - youtube.base.url= (i.e. https://www.youtube.com/)
    - adblock.path= (chrome crx extension can be got from http://chrome-extension-downloader.com/ via id gighmmpiobklfepjocnamgkkbiglidom)
    - webdriver.implicity.wait= (optional)
- execute jar via 'java -jar' command with 1 argument - path to *.txt properties. i.e. 'java -jar youtube-ad-sniffer-0.1-SNAPSHOT-jar-with-dependencies.jar props.txt'

Have fun =)
