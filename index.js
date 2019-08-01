
import { NativeModules } from 'react-native';

var RNClientCertificateFetch = NativeModules.RNClientCertificateFetch;

export default {
    fetchPost: function(alias, url, basicAuth, soapAction, postData) {
      return RNClientCertificateFetch.fetchPost(alias, url, basicAuth, soapAction, postData);
    },
    getCertAlias: function() {
      return RNClientCertificateFetch.getCertAlias();
    }
}