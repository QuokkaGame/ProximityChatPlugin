# 🔊 Proximity Chat Plugin

![Java](https://img.shields.io/badge/Java-21-red) ![Paper](https://img.shields.io/badge/Paper-1.21+-blue) ![License: MIT](https://img.shields.io/badge/License-MIT-green) ![LuckPerms](https://img.shields.io/badge/LuckPerms-Supported-brightgreen)

## 🚀 Overview
**ProximityChatPlugin** adds **proximity-based text chat** to Minecraft!  
Players can only see messages from nearby players, making communication **more immersive and realistic**.

⚠️ **Note:** This plugin **should not be used together with LuckPermsChatFormatter**, as they may conflict.

## ✨ Features
✅ **Proximity-based text chat**: Messages are only visible to nearby players  
✅ **LuckPerms Integration**: Supports **prefixes & suffixes**  
✅ **Fully configurable**: Customize the chat format in `config.yml`  
✅ **Simple reload command**: Apply config changes instantly

## 📥 Installation
1. **[Download the latest version](https://github.com/QuokkaGame/ProximityChatPlugin/releases)**
2. Place the `.jar` file into your `plugins/` folder
3. Restart your Minecraft server

## ⚙️ Configuration (`config.yml`)
```yaml
format: "<{prefix}{name}{suffix}> {message}"
```
```bash
/proximitychat reload
```
## 🛑 Compatibility Warning
This plugin **is not compatible with LuckPermsChatFormatter.**
Using both plugins together **may cause unexpected behavior.** Choose one!
## 📜 License
This project is licensed under the MIT License.
See [`LICENSE`](LICENSE) for more details.
