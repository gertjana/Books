import _root_.java.net.URLEncoder;
import _root_.java.text.DateFormat;
import _root_.java.text.SimpleDateFormat;
import _root_.java.util.Calendar;
import _root_.java.util.TimeZone;

import _root_.javax.crypto.spec.SecretKeySpec;
import _root_.javax.crypto.Mac;

import _root_.org.apache.commons.codec.binary.Base64;

import scala.collection.immutable.SortedMap;
import scala.collection.immutable.TreeMap;

trait SignedRequestsAmazonApi {

    val awsAccessKeyId = "YOUR ACCESS KEY";
    val awsAssociateTagKey = "YOUR TAG";
    val awsSecretKey = "YOUR SECRET KEY";

    val UTF8_CHARSET = "UTF-8";
    val HMAC_SHA256_ALGORITHM = "HmacSHA256";
    val REQUEST_URI = "/onca/xml";
    val REQUEST_METHOD = "GET";

    val endpoint = "ecs.amazonaws.com";
    val service = "AWSECommerceService";
    val version = "2009-07-01";

    val secretKeySpec = new SecretKeySpec(awsSecretKey.getBytes(UTF8_CHARSET), HMAC_SHA256_ALGORITHM);

    def sign(params: Map[String, String]) :String = {

        var sortedParamMap = TreeMap.empty[String, String]
        sortedParamMap = sortedParamMap.insert("AWSAccessKeyId", awsAccessKeyId)
        sortedParamMap = sortedParamMap.insert("AssociateTag", awsAssociateTagKey)
        sortedParamMap = sortedParamMap.insert("Service", service)
        sortedParamMap = sortedParamMap.insert("Version", version)
        sortedParamMap = sortedParamMap.insert("Timestamp", timestamp())
        params foreach ( (o) => sortedParamMap = sortedParamMap.insert(o._1, o._2))

        val canonicalQS = canonicalize(sortedParamMap);
        val toSign = REQUEST_METHOD + "\n" + endpoint + "\n" + REQUEST_URI + "\n" + canonicalQS;
        val hmacStr = hmac(toSign);
        val sig = percentEncodeRfc3986(hmacStr);

        "http://" + endpoint + REQUEST_URI + "?" + canonicalQS + "&Signature=" + sig;
    }

     protected def hmac(stringToSign: String) :String = {

        try {

            val mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(secretKeySpec);

            val data = stringToSign.getBytes(UTF8_CHARSET);
            val rawHmac = mac.doFinal(data);

             val encoder = new Base64();
             return encoder.encodeToString(rawHmac).trim();

        } catch {
              case _ => throw new RuntimeException(UTF8_CHARSET + " is unsupported!")
        }
    }

    protected def timestamp(): String = {

        val dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dfm.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dfm.format(Calendar.getInstance().getTime());

    }

    protected def canonicalize( sortedParamMap: SortedMap[String, String]): String = {

      sortedParamMap.map[String] {
        (key) => percentEncodeRfc3986( key._1 ) + "=" + percentEncodeRfc3986(key._2)
      }.reduceLeft[String]{ (acc, url) => acc + "&" + url }

    }

    protected def percentEncodeRfc3986(s: String): String = {
        try {
            return URLEncoder.encode(s, UTF8_CHARSET).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
        } catch  {
            case _ => throw new RuntimeException(UTF8_CHARSET + " is unsupported!")
        }
    }
}