# Mineberry Gradient Chat

A client-side Fabric mod for Minecraft **26.1.2**. It formats normal outgoing chat messages with a **purple-to-pink** per-character gradient using server-style `&#RRGGBB` codes.

## Important limitations

- This only changes messages typed in normal chat. It does not change your username, rank, or other players' messages.
- The server must accept `&#RRGGBB` formatting from your account/rank. A client mod cannot enable a server feature or bypass rank permissions. Test with a short message first.
- Every letter adds eight formatting characters. To avoid exceeding common chat length limits, if the formatted message would be longer than 256 characters, this mod sends it unchanged.
- Type `!plain your message` to send one message without a gradient. The `!plain ` prefix is removed before sending.
- This mod does not use commands or send messages automatically; it only formats the message you type.

## Build requirements

Build on a computer (or a compatible remote Java build environment) with:
- **JDK 25**
- **Gradle 9.6.1 or newer**
- Internet access for Gradle to download Minecraft, Fabric Loader, Fabric API, and Loom dependencies

Minecraft 26.1.2 is unobfuscated, so this project uses the newer `net.fabricmc.fabric-loom` plugin and Java 25.

## Build

Open a terminal in this project folder and run:

```sh
gradle build
```

The compiled mod should appear at:

```text
build/libs/mineberry-gradient-chat-1.0.0.jar
```

If you have a different Gradle version, install Gradle 9.6.1 or newer and ensure `gradle --version` reports Java 25.

## Install in MojoLauncher (Android)

1. Build the JAR on a computer or remote build environment first. This source project is not itself a mod JAR.
2. Copy `build/libs/mineberry-gradient-chat-1.0.0.jar` to your phone.
3. In your MojoLauncher instance for **Fabric 26.1.2**, open the instance's `mods` folder and put the JAR there.
4. Make sure that same instance has **Fabric API 0.152.1+26.1.2** installed.
5. Launch the game and join `mc.mineberry.org` OPs1.
6. Test with a short message, such as `Hello!`. If the server strips the codes or displays them literally, Mineberry does not accept this format for your chat path/rank and the client mod cannot force it.

## Example output

Typing:

```text
Hello
```

produces a sequence like:

```text
&#8A2BE2H&#A044D6e&#B65DCAl&#CC76BEl&#E28FB2o
```

The exact shade is interpolated per non-space character from `#8A2BE2` to `#FF69B4`.


## Build it without a computer (GitHub Actions)

If you only have your Android phone, you can use GitHub's cloud build:

1. Create a GitHub account if needed, then create a **new repository**.
2. Upload the contents of this ZIP into the repository (not the ZIP file itself). Include the hidden `.github` folder; if your phone's upload flow hides hidden folders, use GitHub's web editor to add `.github/workflows/build.yml` from this project.
3. Open the repository's **Actions** tab and select **Build Mineberry Gradient Chat**.
4. Tap **Run workflow**. When it finishes, open the successful run and download the `mineberry-gradient-chat` artifact.
5. Extract the downloaded artifact ZIP to get the `.jar`, then install that JAR in the MojoLauncher instance's `mods` folder.

GitHub's interface can vary on mobile. If the workflow fails, open the build log and share the error text so the project can be corrected.
