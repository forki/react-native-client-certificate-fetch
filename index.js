
import { NativeModules } from 'react-native';

var RNClientCertificateFetch = NativeModules.RNClientCertificateFetch;

export default {
    fetchPost: function(alias, url, soapAction, postData) {
      return RNClientCertificateFetch.fetchPost(alias, url, soapAction, postData);
    },
    getCertAlias: function() {
      return RNClientCertificateFetch.getCertAlias();
    }
}