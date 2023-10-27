# Time-Sculptor
Overview
In today's digital age, both Android and iPhone devices come equipped with built-in screen time tracking. Additionally, there's a surge in apps designed to aid focus. Recognizing the central role that time plays in both these contexts, 
I introduce Time Sculptor. My app not only tracks your screen time but also incorporates the Pomodoro Technique to boost your concentration.

## App Architecture Overview
![image](https://github.com/JimmyC035/Time-Sculptor/assets/115048430/d6afa8f6-c27f-4ccd-af57-e73d5090d105)
## Technological Stack
  * MVVM (Model-View-ViewModel)
  * MVI (Model-View-Intent)
  * DI (Hilt)
  * Room
  * SharePreference
  * MPAndroid
  * Corountine / Flow
  * Compose UI
  * WorkManager
  * UsageStateManager
  * FloatingWindowService
  * Unit test

## Features and Demonstrations 
<table>
  <tr>
    <td>
      <img src="https://github.com/JimmyC035/Time-Sculptor/assets/115048430/bebb0df4-3eb9-456b-b2e4-e9538a3acac3" alt="Demo GIF of Time Sculptor" width="150">
    </td>
    <td>
       <h3>Welcome Page</h3>
      <ul>
       <li><strong style="font-size: 1.1 em;">Description:</strong> Upon first launch, users receive a welcome message, assurance of data privacy stored securely on their device, and are prompted to grant necessary permissions for optimal app functionality. It's essential to grant all permissions for the app to reach its full potential.</li>
      </ul>
    </td>
  </tr>
    <tr>
    <td>
      <img src="https://github.com/JimmyC035/Time-Sculptor/assets/115048430/8b024b74-5e21-47cd-b4f4-46895812fa8e" alt="HomePage to detail" width="160">
    </td>
    <td>
      <h3>Home Page</h3>
      <ul>
       <li><strong style="font-size: 1.1 em;">Description:</strong> Time Sculptor presents users with a daily screen time summary, detailed insights per app, an access timeline, notification counts, and launch frequencies for individual apps.</li>
      </ul>
    </td>
  </tr>
    <tr>
    <td>
      <img src="https://github.com/JimmyC035/Time-Sculptor/assets/115048430/7e5dd7e2-843e-4b8c-abf2-92a721ca50db" alt="TodayPage" width="160">
    </td>
    <td>
      <h3>Today's Details</h3> 
      <li><strong style="font-size: 1.1 em;">Description:</strong> In the second tab, users view a full-day timeline of screen activity, their most-used apps with in-depth details, set and modify screen time and pick-up goals, and compare today's and yesterday's phone pick-ups and notifications.</li>
      </ul>
    </td>
  </tr>
   <tr>
    <td>
      <img src="https://github.com/JimmyC035/Time-Sculptor/assets/115048430/8771dd1b-b3c5-474c-9dfe-5ad775b8dd59" alt="HistoryPage" width="160">
    </td>
    <td>
      <h3>History Page</h3>
    <li><strong>Description:</strong> The history section allows users to explore past usage, select specific dates to view daily screen time, identify most-used apps, see app launch counts, and review total notifications for any chosen day.</li>
    </ul>
    </td>
  </tr>
     <tr>
    <td>
      <img src="https://github.com/JimmyC035/Time-Sculptor/assets/115048430/ca667f11-ffa6-46eb-95b1-795dbaf1c67e" alt="Pomodoro" width="160">
    </td>
    <td>
      <h3>Pomodoro Timer</h3>
    <li><strong>Description:</strong> The Pomodoro Timer, offers timer controls, a focus mode overlay to prevent distractions, and options to return or urgently exit the timer, emphasizing sustained focus.</li>
      </ul>
    </td>
  </tr>
       <tr>
    <td>
      <img src="https://github.com/JimmyC035/Time-Sculptor/assets/115048430/e59fa7a0-7d51-4a45-b2c7-2c6a43f07301" alt="Setting" width="160">
    </td>
    <td>
      <h3>Settings Page</h3>
     <li><strong>Description:</strong> The settings page allows users to customize app preferences, including setting daily screen time and device pickup goals, and scheduling daily summary notifications.</li>
      </ul>
    </td>
  </tr>
</table>

## Installation

### Direct APK Installation
1. Download the APK from [download-link](https://drive.google.com/file/d/1IXWKQE8jsjpbSNzzyWSNE_BqoeTaJIfX/view?usp=drive_link).
2. On your Android device, go to `Settings > Security` and enable `Install apps from unknown sources`.
3. Navigate to your Downloads folder and tap the downloaded APK to install.

### From Source
1. Ensure you have [Android Studio](https://developer.android.com/studio) installed.
2. Clone the repository:
   ``` git clone git@github.com:JimmyC035/Time-Sculptor.git ```
3. Open the project in Android Studio.
4. Build and run the project on an emulator or real device.



