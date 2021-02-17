# react-native-thub-rn-barcode-scanning

Rn module for barcode scanning

## Installation

```sh
npm install react-native-thub-rn-barcode-scanning
```

## Usage

```js
import ThubRnBarcodeScanning from 'react-native-thub-rn-barcode-scanning';

ThubRnBarcodeScanning.scanBarcode(
  imagePath,
  (data) => {
    console.log(data);
  },
  (errorMessage) => {
    console.log(errorMessage);
  }
);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
