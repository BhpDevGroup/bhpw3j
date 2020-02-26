bhp-java接入
# 1.获取地址,私钥,公钥

1.将 [bhpw3j.rar]解压后的文件放入本地maven仓库的io目录中

2.在项目pom.xml中引入:

```xml
<dependency>
    <groupId>io.bhpw3j</groupId>
    <artifactId>core</artifactId>
    <version>2.3.0</version>
</dependency>
```

3.Example:

```java
package com.example.demo;

import io.bhpw3j.crypto.ECKeyPair;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class ECKeyPairExmaple {

    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair ecKeyPair = ECKeyPair.createEcKeyPair();
//        钱包导入字符串
        String walletImportFormat = ecKeyPair.exportAsWIF();
//        私钥
        String privateKey = ecKeyPair.getPrivateKey().toString();
//        公钥
        String publicKey = ecKeyPair.getPublicKey().toString();
//        地址
        String address = ecKeyPair.getAddress();
    }
}
```

# 2.构造交易:

1.在项目pom.xml中引入:

```xml
<dependency>
            <groupId>io.bhpw3j</groupId>
            <artifactId>wallet</artifactId>
            <version>2.3.0</version>
</dependency>
<dependency>
            <groupId>io.bhpw3j</groupId>
            <artifactId>crypto</artifactId>
            <version>2.3.0</version>
</dependency>
```

2.Exmaple:
```java
package com.example.demo;

import io.bhpw3j.crypto.transaction.RawTransactionOutput;
import io.bhpw3j.model.types.BhpAsset;
import io.bhpw3j.protocol.Bhpw3j;
import io.bhpw3j.protocol.http.HttpService;
import io.bhpw3j.utils.Numeric;
import io.bhpw3j.wallet.Account;
import io.bhpw3j.wallet.AssetTransfer;
import io.bhpw3j.wallet.Utxo;
import lombok.Data;

import java.util.Arrays;
import java.util.List;


public class CreateTransactionExmaple {

    static final String HPAX_ASSET_ID = "0xe3fd29ec39d45ddba437e219a93bbbbb803f9cc4cc2b828b2115675f1b8c8ba1";

    static final String HPAX_UTXO_TXID = "0x08f4e2754330969fce6be43591187c4b17941a9a460cd41ebe7fc3c88adef8a8";

    static final String BHP_UTXO_TXID = "0x657068b87a8345d0dd19bf31fc8b95bb9f8b1504410658bca18626aec0e141e0";

    static final String OUTPUT_ADDRESS = "AZ5nDD1U8iVPkfXDgRGwBhMbqxSPZh1h5R";

    public static void main(String[] args) {
        Account account = Account.fromWIF("L5aP4GDfYswWUdNKUrGodPaWxddA5HaVzdMeAGNR969JVvdTgb9F").build();
//        获取utxo
        Utxo utxo1 = new Utxo(HPAX_ASSET_ID,HPAX_UTXO_TXID,0,11);
        Utxo utxo2 = new Utxo(BhpAsset.HASH_ID,BHP_UTXO_TXID,0,10);
//        构建输出
        List<RawTransactionOutput> outputs = createOutputs();
        Bhpw3j bhpw3j = Bhpw3j.build(new HttpService("192.168.1.174:20557"));
//        签名
        AssetTransfer assetTransfer = new AssetTransfer
                .Builder(bhpw3j)
                .account(account)
                .utxos(utxo1, utxo2)
                .outputs(outputs)
                .build()
                .sign();
//        获得签名后的交易
        String transaction = Numeric.toHexStringNoPrefix(assetTransfer.getTransaction().toArray());
        System.out.println(transaction);
    }

    private static List<RawTransactionOutput> createOutputs() {
        RawTransactionOutput output1 = new RawTransactionOutput(HPAX_ASSET_ID, "1", OUTPUT_ADDRESS);
        RawTransactionOutput output2 = new RawTransactionOutput(BhpAsset.HASH_ID, "1", OUTPUT_ADDRESS);
        return Arrays.asList(output1, output2);
    }
}

```