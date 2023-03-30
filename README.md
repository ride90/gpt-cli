# gpt-cli
Simple command line ChatGPT helper.
You can ask ChatGPT directly from your terminal emulator and get ready to execute command as a reply.
The tool sends additional instructions and information about your shell/OS. It'll help chatGPT to reply with ready to run command.

### Examples:

```shell
gptc "see all java processes"
Command suggestion:

ps aux | grep java
```

```shell
gptc "see all java processes listening port 8080"
Command suggestion:

lsof -i :8080 -n -P -t | xargs -I {} sh -c "ps -p {} -o comm="
```

```shell
> gptc "list all files including hidden"
Command suggestion:

ls -a
```

```shell
gptc "list all files using table with date in human friendly format"
Command suggestion:

ls -laht
```

```shell
gptc "glue video_one.mp4 with video_two.avi and add wallpaper.jpg at the end. Output it as a HD mp4 video using ffmpeg."
Command suggestion:

ffmpeg -i video_one.mp4 -i video_two.avi -i wallpaper.jpg -filter_complex "[0:v] [0:a] [1:v] [1:a] [2:v] [2:a]concat=n=3:v=1:a=1 [v] [a]; [v]scale=w=1920:h=1080:force_original_aspect_ratio=decrease,crop=1920:1080[vout]" -map "[vout]" -map "[a]" -c:v libx264 -preset medium -crf 18 -c:a aac -b:a 192k -shortest output.mp4
```

```shell
gptc "run latest elastic search using docker with mounted volume and name it es777"
Command suggestion:

docker run -d -v /path/to/your/data:/usr/share/elasticsearch/data --name es777 -p 9200:9200 -p 9300:9300 docker.elastic.co/elasticsearch/elasticsearch:7.13.1
```

```shell
gptc "give me a joke about java and output it as a python code"
Command suggestion:

print("Java is like a cup of coffee, it's good until you start throwing exceptions.")
```

##### It is possible to ask a general question (non-executable reply):

```shell
gptc "Where I can find Jet in New Reno?" -g
Reply:

You can find Jet in New Reno by visiting the drugstore in the eastern part of the city, near the train station. It's called the "Golden Globes". Just head east from the train station until you see a sign for the store. Once inside, ask the pharmacist for Jet and they should have some in stock.
```

## Installation

### 1. Get executable
- Cet a built **jar** file or build it from **sources** (see [releases](https://github.com/ride90/gpt-cli/releases))
- _Optional_: alias it in your .bashrc/.zshrc `alias gptc="java -jar /path/to/gpt-cli-0.1.jar"` (or add it to your **PATH**)

### 2. API key
- Get [openai api key](https://platform.openai.com/account/api-keys)
- Set environment variable `export OPENAI_API_KEY="YOUR-KEY-IS-HERE"`
- _Optional_: it makes sense to set env variable in your .bashrc/.zshrc

## Option
To see list of options: `gptc --help` 





