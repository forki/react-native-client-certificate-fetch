
# react-native-client-certificate-fetch

## Getting started

`$ npm install react-native-client-certificate-fetch --save`

### Mostly automatic installation

`$ react-native link react-native-client-certificate-fetch`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-client-certificate-fetch` and add `RNClientCertificateFetch.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNClientCertificateFetch.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.react_native_client_certificate_fetch.RNClientCertificateFetchPackage;` to the imports at the top of the file
  - Add `new RNClientCertificateFetchPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-client-certificate-fetch'
  	project(':react-native-client-certificate-fetch').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-client-certificate-fetch/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      implementation project(':react-native-client-certificate-fetch')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNClientCertificateFetch.sln` in `node_modules/react-native-client-certificate-fetch/windows/RNClientCertificateFetch.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Client.Certificate.Fetch.RNClientCertificateFetch;` to the usings at the top of the file
  - Add `new RNClientCertificateFetchPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNClientCertificateFetch from 'react-native-client-certificate-fetch';

// TODO: What to do with the module?
RNClientCertificateFetch;
```
