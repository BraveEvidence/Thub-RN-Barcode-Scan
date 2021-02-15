import { NativeModules } from 'react-native';

type ThubRnBarcodeScanningType = {
  multiply(a: number, b: number): Promise<number>;
};

const { ThubRnBarcodeScanning } = NativeModules;

export default ThubRnBarcodeScanning as ThubRnBarcodeScanningType;
