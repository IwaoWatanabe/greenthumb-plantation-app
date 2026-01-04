

## JARライブラリの入手

```cmd
gradlew :h2db:copylib
```

## 対話型の操作

```cmd
cd GreenthumbNursery

java -cp ..\h2db\lib\h2-1.4.197.jar org.h2.tools.Shell ^
  -url "jdbc:h2:~/greenthumb.h2" -user sa
```

次のような出力があります。`sql>` がプロンプトで、ここにSQLをタイプします。

```cmd
Welcome to H2 Shell 1.4.197 (2018-03-18)
Exit with Ctrl+C
Commands are case insensitive; SQL statements end with ';'
help or ?      Display this help
list           Toggle result list / stack trace mode
maxwidth       Set maximum column width (default is 100)
autocommit     Enable or disable autocommit
history        Show the last 20 statements
quit or exit   Close the connection and exit

sql>
```

## スキーマの作成と確認

H2のスキーマ作成用に初期化スクリプトを用意しています。
次の操作でSQLスクリプトを実行できます。

```sql
RUNSCRIPT FROM 'init-h2.sql';
```

生成されたテーブルの確認は以下。

```sql
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC';
```

対話SQLの終了は以下。
これを抜けないと、アプリ起動時にエラーとなるので注意してください。

```sql
quit;
```

H2をサーバモードで起動して、それに接続するようにすれば
アプリを起動しながら対話SQLを平行利用することは可能です。

## アプリの起動(開発中)

```cmd
.\gradlew.bat :GreenthumbNursery:run
```

## アプリの起動

```cmd
.\gradlew.bat :GreenthumbNursery:jar

java -jar .\GreenthumbNursery\build\libs\GreenthumbNursery.jar
```

※ GreenthumbNursery.jar と同じフォルダに h2-1.4.197.jar を配置する必要があります。


