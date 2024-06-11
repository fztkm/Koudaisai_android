# Android版工大祭アプリ
## [🔗動作デモスライドはこちら](https://www.canva.com/design/DAFSl1gI8vM/k39NhI8_oRRyMybJ7yBlXQ/view?utm_content=DAFSl1gI8vM&utm_campaign=designshare&utm_medium=link&utm_source=publishpresent)


<img width="826" alt="画面一覧の画像" src="https://user-images.githubusercontent.com/74134260/206072346-2545ebdb-052a-4a1a-a022-1fad8cd358ef.png">



## 1. 概要
第60回工大祭(2022年11月実施)において実際に使用した予約・企画情報閲覧のAndroidモバイルアプリ

工大祭は2日程の開催で，各日程2000人の入場制限が設けられており，その予約システムとしてiOS・Androidアプリが作られました．iOS版については別の方が開発されました．


❗開発は別のプライベートリポジトリで行いました．  本リポジトリのソースコード/アセットは，公開用に一部改変したものです．❗



## 2. 主な機能
- 予約/予約情報確認
- QRコード入場
- タイムテーブル
- 企画情報一覧
- 地図
- 設定

機能の詳細は下記に記載

## 3. 使用技術
- Kotlin
- Navigation Compose
- Jetpack Compose
- ViewModel
- Dagger Hilt
- Maps SDK for Android
- Maps Compose
- Coroutine
- CameraX
- Zxing
- Firebase
  - Authentication
  - Cloud Firestore
  - Dynamic Links

## 4. 開発
### 4-1. 概要
開発期間は2.5ヶ月で，Android版の実装を担当しました．  
工大祭アプリの開発チーム自体は複数人で行っており，全体的な仕様や土台となるデザインは，都度，話し合いながら策定しました．

### 4-2. スケジュール
<table>
    <tr>
      <td>8月中旬</td>
      <td>開発開始</td>
    </tr>
    <tr>
      <td>10月10日</td>
      <td>アプリ一般公開(予約関連機能のみ)</td>
    </tr>
    <tr>
      <td>10月30日</td>
      <td>新規予約開始</td>
    </tr>
    <tr>
      <td>11月9日</td>
      <td>全機能公開</td>
    </tr>
    <tr>
      <td>11月19・20日</td>
      <td>工大祭当日</td>
    </tr>
</table>

### 4-3. 面白かった点
　このアプリの開発を機にJetpack Composeを初めて使用しましたが，UIの実装がxmlに比較すると，とても簡単で実装も早くできたので楽しく開発できました．  
その他にも，Dagger HiltやMaps SDK for Android, Coroutineも初めて使用しました．実際に解決した課題に対して，これを使うと良さそうだなという感じで取り入れ始めました．
明確な目的がある上での学習であったため，とても身に付いている気がして良い体験になりました．  
　例として，Coroutineについては，予約処理の部分でコールバックネストが深くなりコードが読みづらくなったため，これを解消しようということで取り入れました．  
　このアプリは工大祭で一度のみ使用されること，開発者は自分だけであることから，高い保守性と厳密なアーキテクチャを必要とするものではないです．
しかし，新たに勉強しながら実装したので，読みやすさや保守性，拡張性の点で課題が残っています．より良いプラクティスを探していきたいです．

### 4-4. 困難だった点
　仕様が明確に決まらない状態にあったことが困難でした．予約の方法や必要な個人情報の種類などは，学祭委員会の方と大学側で考える形だったため，開発側もそれに合わせて柔軟に対応する必要がありました．
また，表示したいコンテンツ情報の詳細等についても出揃うまで待つ必要がありました．
しかし，開発自体は大きく躓くところもなく順調に進められました．
予約と工大祭当日の運用についても，全体を通して問題もほとんど起きず，スムーズに終えることができました．
　

### 4-5. 工夫点
　ある機能を実装する時に，ユーザーがその機能を直感的に使えることが重要だと思いました．
機能と要素の関連性を意識し，関連度の高いものは近い位置に配置しました．  
　例えば，予約情報と編集機能，QR表示は関連度が高いためひとまとまりの構成としました．
その他にも，「任意の企画とそれが行われる場所」，「任意の場所とそこで行われる企画」は，ユーザーが知りたい情報であると考えたため，企画->場所，場所->企画の導線をそれぞれ実装しました．

## 5. 機能の詳細
　主に5つの画面から構成されいています．ホーム画面，企画一覧画面，地図画面，タイムテーブル画面，設定画面です．
### [🔗動作デモスライドはこちら](https://www.canva.com/design/DAFSl1gI8vM/k39NhI8_oRRyMybJ7yBlXQ/view?utm_content=DAFSl1gI8vM&utm_campaign=designshare&utm_medium=link&utm_source=publishpresent)
### ホーム画面の機能
```
1. 新規予約
  必要な個人情報と希望入場日を入力
  予約に空きがあれば，アカウントを作成してDBを更新
  非同期処理，トランザクション処理
  
2. ログイン
  予約済みのユーザがメールアドレス,パスワードを入力してログインする
  パスワードリセットメールを送信する
  
3. 予約情報の確認
  予約情報閲覧・編集
    ・ 編集キャンセル時の情報復元
  同伴者の追加予約 (スマホがない方の利用を想定): ログインユーザーに紐付け
  QRコードの表示
  
4. 受付係用QRコード読み取り
  受付用権限の付与
    ・ DynamicLink
    ・ 管理者属性を付与したユーザーを作成orログインユーザーに属性を付与
  受付処理
    ・ QRコードを読み取り入場判定
    ・ 入場時のタイムスタンプを付与
```
### 企画一覧画面の機能
```
1. 企画を一覧表示
2. 謎解きのクリア率表示，トライアスロンのランキングのWebView
3. 企画詳細
4. 企画詳細から実施場所の地図を表示
```
### 地図画面の機能
```
1. 企画場所等にピンを配置
2. ピンを押したら，その場所の企画を一覧表示
3. さらに各項目から企画詳細に遷移可能
4. 現在地の表示
```
### タイムテーブル画面の機能
```
1. スケジュールを表示
2. １日目と2日目でタブ切り替え
```
### 設定画面の機能
```
1. 各種リンクを設置
2. ログアウト
3. パスワード再設定メール
4. 予約情報の全削除
```

