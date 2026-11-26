
const friday = {
  speak(message) {
    if ("speechSynthesis" in window) {
      speechSynthesis.cancel();
      const voice = new SpeechSynthesisUtterance(message);
      voice.lang = "en-US";
      speechSynthesis.speak(voice);
    }
  },

  startVoice() {
    const SpeechRecognition =
      window.SpeechRecognition || window.webkitSpeechRecognition;

    if (!SpeechRecognition) {
      this.speak("Voice recognition is not supported in this browser.");
      return;
    }

    const recognition = new SpeechRecognition();
    recognition.lang = "en-US";

    recognition.onresult = (event) => {
      const command = event.results[0][0].transcript.toLowerCase();
      this.handleCommand(command);
    };

    recognition.onerror = () => {
      this.speak("Please try again.");
    };

    recognition.start();
  },

  handleCommand(command) {
    if (command.includes("hello") || command.includes("hi")) {
      this.speak("Hello! I am FRIDAY. How can I help you?");
    } else if (command.includes("time")) {
      this.speak("The time is " + new Date().toLocaleTimeString());
    } else if (command.includes("date")) {
      this.speak("Today is " + new Date().toDateString());
    } else if (command.includes("open maps")) {
      window.open("https://www.google.com/maps", "_blank");
      this.speak("Opening Maps.");
    } else if (command.includes("open gallery")) {
      window.open("https://photos.google.com", "_blank");
      this.speak("Opening Gallery.");
    } else {
      this.speak("I heard you. This command is not available yet.");
    }
  }
};

window.friday = friday;
