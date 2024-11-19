# Android版工大祭アプリ
## [🔗動作デモスライドはこちら](https://www.canva.com/design/DAFSl1gI8vM/F7eH6ETyVh0lpwLReMnhZA/view?utm_content=DAFSl1gI8vM&utm_campaign=designshare&utm_medium=link&utm_source=editor)


<img width="826" alt="画面一覧の画像" src="https://user-images.githubusercontent.com/74134260/206072346-2545ebdb-052a-4a1a-a022-1fad8cd358ef.png">
<img width="550" alt="Koudaisai_Application_Components" src="https://github.com/user-attachments/assets/9285d0b3-c8c5-4dca-a421-cde67be10787">

## 1. 概要
第60回工大祭(2022年11月実施)において実際に使用した予約・企画情報閲覧のAndroidモバイルアプリ

工大祭は2日程の開催で，各日程2000人の入場制限が設けられており，その予約システムとして作られました．

Web (2人)/ iOS (2人)/ Android (1人) のチーム開発であり，
私はAndroid版の開発とFirebaseの設定を担当しました．

開発期間は2.5ヶ月です．

全体的な仕様や土台となるデザインは，各OSで共通しており，全員で話し合いながら策定．

❗開発は別のプライベートリポジトリで行いました．本リポジトリのソースコード/画像等は，公開用に一部改変したものです．❗



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

## 4. スケジュール
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

## 5. 機能の詳細
　主に5つの画面から構成されいています．ホーム画面，企画一覧画面，地図画面，タイムテーブル画面，設定画面です．
### [🔗動作デモスライドはこちら](https://www.canva.com/design/DAFSl1gI8vM/F7eH6ETyVh0lpwLReMnhZA/view?utm_content=DAFSl1gI8vM&utm_campaign=designshare&utm_medium=link&utm_source=editor)
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

