# kmp-starter-template-refined

---

## Introduction

If you're interested in Kotlin Multiplatform (Compose Multiplatform) with Clean Architecture, feel free to take a look.

Kmp-Starter-Template  
https://github.com/DevAtrii/Kmp-Starter-Template

This project is for learning and technical practice.

---

## Preview

<p align="left">
  <img src="https://i.postimg.cc/yxdwSqY7/Screenshot-20260503-223226.png" width="160"/>
  <img src="https://i.postimg.cc/zvB9RmXN/Screenshot-20260503-223231.png" width="160"/>
  <img src="https://i.postimg.cc/0jQLJg5T/Screenshot-20260503-223240.png" width="160"/>
  <img src="https://i.postimg.cc/5y0ZFcN1/Screenshot-20260503-223243.png" width="160"/>
</p> 

---

## Features

---

## Tech Stack

- Kotlin Multiplatform  
  Shared Android / iOS application foundation (Uses commonMain for shared business logic, UI foundation, persistence abstractions, and expect/actual contracts to isolate platform-specific providers such as DataStore, database setup, intents, screen utilities, and native integrations)
- Modular Clean Architecture  
  Feature-isolated layered Gradle module graph (Organizes features into independent data, domain, and presentation modules, where domain owns repository contracts and use-case logic, data provides concrete implementations, and presentation depends inward on domain APIs instead of reaching data directly)
- MVI ViewModel  
  Unidirectional state management with typed UI contracts (Uses MviViewModel<STATE, ACTIONS, EVENTS> to model screen state, user actions, and one-time events, forcing UI interactions through a centralized onAction() entry point while updating state through StateFlow-based immutable state transitions)
- Navigation3 with ResultStore  
  Type-safe Navigation3 back stack infrastructure (Defines routes as @Serializable NavKey screens, registers screen subclasses for saveable back stack restoration, renders destinations through NavDisplay with Koin entry providers, and wraps navigation operations inside StarterNavigator with ResultStore support for screen-to-screen result passing)
- Build Logic Convention Plugins  
  Programmatic Gradle module standardization (Implements reusable Plugin<Project> convention plugins such as CommonPlugin, ComposeMultiplatformPlugin, KoinPlugin, and KoinComposePlugin to apply shared plugins, dependencies, and Compose/Koin configuration consistently across feature modules)

---

## Environment

---

## Credits

This project is created for independent learning and demonstration purposes.
Special thanks to the original author for their open-source contribution.

---

## Notes

Image resources are for learning and purposes only. Please do not use them for commercial purposes.

If there is any infringement, please contact me for removal. Thank you.

---

## License

This repository is intended for learning and demonstration.

If you plan to open-source it, please choose a license and confirm third-party asset usage rights.

---

## Project Structure

```
```
