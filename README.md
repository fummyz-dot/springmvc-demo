# springmvc-demo

WildFly 42 上で動く、Spring Boot を使わない素の Spring MVC の簡易デモ。

## 環境

| 項目 | バージョン |
|---|---|
| Java | 21 |
| WildFly | 42.0.0.Final（`/home/nabe/opt/wildfly/current`） |
| Spring Framework (spring-webmvc) | 7.0.10 |
| Thymeleaf (thymeleaf-spring6) | 3.1.5.RELEASE |
| Gradle (Wrapper) | 9.9.0 |

- URL: http://localhost:8080/springmvc-demo/hello
- アプリログ: `$WILDFLY_HOME/standalone/log/springmvc-demo.log`（war同梱のLogbackで出力）

## ビルド

```bash
./gradlew clean war
```

`build/libs/springmvc-demo.war` が生成される。

## 1. ローカルデプロイ（開発・デバッグ用）

VS Code からビルド、デプロイ、デバッガ接続を一括で行う。

1. WildFly をデバッグモードで起動する
   ```bash
   standalone.sh --debug 8787
   ```
2. VS Code の「実行とデバッグ」（Ctrl+Shift+D）で **Attach to WildFly (8787)** を選び、F5 を押す
   - `deploy` タスクが war をビルドし、`standalone/deployments/` にコピーする
   - WildFly のデプロイメントスキャナが自動でデプロイする
3. ブラウザで動作を確認する。ブレークポイントも使用できる
4. 停止: デバッガは Shift+F5、WildFly は起動したターミナルで Ctrl+C

> 8787 はデバッガ接続用のポート。ローカルでのみ使用し、本番では `--debug` を付けない。

## 2. CLI デプロイ（本番に近い手順）

1. WildFly を通常モードで起動する
   ```bash
   standalone.sh
   ```
2. ビルドしてデプロイする
   ```bash
   ./gradlew clean war
   jboss-cli.sh --connect --command="deploy build/libs/springmvc-demo.war --force"
   ```
3. デプロイ状態を確認する（`OK` であればよい）
   ```bash
   jboss-cli.sh --connect --command="deployment-info"
   ```
4. アンデプロイする
   ```bash
   jboss-cli.sh --connect --command="undeploy springmvc-demo.war"
   ```

## 注意事項

- **1 と 2 の方式を同時に使わない。** 同じ名前の war を両方の方式で扱うと衝突する。
  - 1 → 2 に切り替えるとき: `rm -f $WILDFLY_HOME/standalone/deployments/springmvc-demo.war*`
  - 2 → 1 に切り替えるとき: `jboss-cli.sh --connect --command="undeploy springmvc-demo.war"`
- CLI でデプロイしたアプリは WildFly の設定に登録されるため、再起動後も自動でデプロイされる。
- ログは WildFly に任せず、war 同梱の Logback で出力している。WildFly の logging サブシステムと SLF4J モジュールは `WEB-INF/jboss-deployment-structure.xml` で除外している。
- Gradle Wrapper の取得先は `downloads.gradle.org`。`services.gradle.org` は 504 が出たため使用していない。