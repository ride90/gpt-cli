package io.ridelink.gpt;

class GPTMessage {
    private GPTMessage() {
    }

    public static String build(String question) throws GPTMessageException {
        return """
                While answering my question, you MUST follow next rules:
                - Return only a valid code which I can copy and paste into my terminal emulator, and it must work
                - Return code only in a plaintext
                - Don't include any comments or explanations
                - You can link several commands together
                - Provide only one solution
                - Don't add any styling, HTML or markup. Only plaintext!
                - Be a converter from the human language to the machine code

                Information about my system:
                - shell is "%s"
                - the operating system is "%s"
                Question: %s
                """.formatted(getShell(), getOS(), question);
    }

    private static String getOS() {
        return System.getProperty("os.name");
    }

    private static boolean isWindows() {
        return (getOS().toLowerCase().contains("win"));
    }

    private static String getShell() throws GPTMessageException {
        String shell = System.getenv("SHELL");
        if (shell != null) {
            return shell;
        } else if (isWindows()) {
            return "PowerShell";
        }
        throw new GPTMessageException("Shell was not detected.");
    }

}
