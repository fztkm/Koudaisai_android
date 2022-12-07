package com.nit.koudaisai.local_database

import com.nit.koudaisai.R
import com.nit.koudaisai.domain.model.Event
import com.nit.koudaisai.domain.model.EventContent
import com.nit.koudaisai.domain.model.LocationId

//TODO 暫定
object EventDatabase {
    val specialEventList = listOf(
        Event(
            name = "声優トークショー",
            img = null,
            date = "11/20(日) 14:00 ~ 15:00\n(開場:12:30)",
            location = "51号館5111講義室",
            locationId = LocationId.FIFTYONE,
            latLngList = mapOf("51号館5111講義室" to CommonLatLng.fiftyOne),
            description = listOf(
            )
        ),
        Event(
            name = "中夜祭",
            img = null,
            date = "11/19(土) 16:30 ~ 18:30",
            location = "2号館ステージ前",
            locationId = LocationId.STAGE,
            latLngList = mapOf(
                "2号館ステージ前" to CommonLatLng.stage,
                "整理券配布場所 (晴天)" to CommonLatLng.ticket,
                "整理券配布場所 (雨天)" to CommonLatLng.nitechHallFront
            ),
            description = listOf(
                EventContent(
                    title = "入場制限", text = "観覧エリアに入場するには整理券が必要です."
                ),
                EventContent(
                    subtitle = "配布場所 (晴天時)", img = R.drawable.haifu
                ),
                EventContent(
                    subtitle = "配布場所 (雨天)", text = "NITech Hall前"
                ),
                EventContent(
                    subtitle = "配布時間", text = "・11:00 ~\n" +
                            "・13:00 ~\n" +
                            "・15:00 ~\n※ 各時間100枚ずつ配布し、なくなり次第終了します。"
                ),
                EventContent(
                    title = "企画内容", text = "1. ネタ披露、ミニゲーム\n" +
                            "2. 抽選会"
                ),
            )
        ),
        Event(
            name = "NIT LIVE FES 2022",
            img = null,
            date = "11/20(日) 12:30 ~ 15:30",
            location = "NITech Hall",
            locationId = LocationId.NIT,
            latLngList = mapOf("NITech Hall" to CommonLatLng.nitechHall),
            description = listOf(

            )
        ),
    )
    val generalEventList = listOf(
        Event(
            name = "お化け屋敷",
            img = null,
            date = "11/19(土) 10:00 ~ 16:00\n11/20(日) 10:00 ~ 15:30",
            location = "12号館およびその周辺",
            locationId = LocationId.TWELVE,
            latLngList = mapOf("12号館およびその周辺" to CommonLatLng.twelve),
            description = listOf(
                EventContent(
                    title = "募集内容",
                    text = "長年、人間は立ち入り禁止になっていた場所、そんな場所に入ってみたいという" +
                            "好奇心旺盛な方をお待ちしております。もちろん何が起こるかは分からない のでご注意ください。",
                ),
                EventContent(
                    title = "注意事項",
                    text = "以下の参加条件を満たす方のみ参加可能です。\n" +
                            "1. 心臓が悪いなどの理由により驚かされても耐えることが可能である\n" +
                            "2. 妊娠しているなどの理由により転倒時に大きな事故につながる心配がない\n" +
                            "3. 小学生以上か、小学生未満の場合は保護者が同行している\n" +
                            "4. 車いすなどではなく1人での移動が容易である"
                )
            )
        ),
        Event(
            name = "ゲームセンター名工",
            img = null,
            date = "11/19(土) 10:00 ~ 16:00\n11/20(日) 10:00 ~ 15:30",
            location = "体育館",
            locationId = LocationId.GYM,
            latLngList = mapOf("体育館" to CommonLatLng.gym),
            description = listOf(
                EventContent(
                    title = "企画内容", text = "参加者には４つのミニゲームに挑戦していただきます。" +
                            "ミニゲームのノルマを達成して景品をゲットしましょう！"
                )
            )
        ),
        Event(
            name = "コミュニティストリート",
            img = null,
            date = "11/19(土) 10:00 ~ 16:00\n11/20(日) 10:00 ~ 15:30",
            location = "体育館",
            locationId = LocationId.GYM,
            latLngList = mapOf("体育館" to CommonLatLng.gym),
            description = listOf(
                EventContent(
                    title = "企画内容",
                    text = "名工大周辺の地域団体と協力してブースを出展します。" +
                            "スタンプラリーの企画ではブースを回って景品をゲットできます！ぜひお越しください！！",
                ),
                EventContent(
                    title = "参加団体",
                    text =
                    "",
                )
            )
        ),
        Event(
            name = "スタンプラリー",
            img = null,
            date = "11/19(土) 10:00 ~ 16:00\n11/20(日) 10:00 ~ 15:30",
            location = "名工大全体(進入禁止場所を除く)",
            locationId = null,
            latLngList = null,
            description = listOf(
                EventContent(
                    title = "企画内容",
                    text = "工大祭の1日目、2日目にスタンプラリーを実施します。" +
                            "いろいろな企画をまわりながらスタンプを集めて景品をゲットしましょう！参加お待ちしています！",
                ),
                EventContent(
                    title = "特典", text = "お菓子や小物",
                ),
                EventContent(
                    title = "注意事項", text = "景品はスタンプを全て集めた方のみにお渡しします。 景品は1人1つまでです。"
                )
            )
        ),
        Event(
            name = "ブース店",
            img = null,
            date = "11/19(土) 10:00 ~ 16:00\n11/20(日) 10:00 ~ 15:30",
            location = "4号館",
            locationId = LocationId.FOUR,
            latLngList = mapOf("4号館" to CommonLatLng.four),
            description = listOf(
                EventContent(
                    title = "企画内容", text = "学外より企業の方や学生団体をお呼びして様々な体験ができます！",
                ),
                EventContent(
                    "出典団体",
                    text = "",
                ),
                EventContent(
                    "特典", text = ""
                )
            )
        ),
        Event(
            name = "プラネタリウム",
            img = null,
            date = "11/19(土) 10:00 ~ 16:00\n11/20(日) 10:00 ~ 15:30",
            location = "19号館ゆめ空間",
            locationId = LocationId.NINTEEN,
            latLngList = mapOf("19号館ゆめ空間" to CommonLatLng.nineteen),
            description = listOf(
                EventContent(
                    "企画内容",
                    text = "プラネタリウムを名工に実現！普段の夜空とは一味違い、" +
                            "名工でしか見ることができない星空を見ることができます！",
                ),
                EventContent(
                    "注意事項", text = "開催時間の5分前までに19号館ゆめ空間に集合！\n" +
                            "当日にゆめ空間の入口の受付で時間指定の予約をしよう！\n" +
                            "整理券がなくなり次第受付終了します\n" +
                            "集団での受付は半分ずつに別れてもらうことがあります"
                )
            )
        ),
        Event(
            name = "学生募集",
            img = null,
            date = "11/19(土) 10:00 ~ 16:00\n11/20(日) 10:00 ~ 15:30",
            location = "52号館",
            locationId = LocationId.FIFTYTWO,
            latLngList = mapOf("52号館" to CommonLatLng.fiftyTwo),
            description = listOf(
                EventContent("企画内容", text = "サークルなどの団体による出典"),
                EventContent(
                    title = "参加団体",
                    text = ""
                )
            )
        ),
        Event(
            name = "学生企画",
            img = null,
            date = "11/19(土)　10:00～13:45",
            location = "2号館前ステージ",
            locationId = LocationId.STAGE,
            latLngList = mapOf("2号館前ステージ" to CommonLatLng.stage),
            description = listOf(
                EventContent(
                    "企画内容", text = "工大祭1日目11月19日(土)にステージ上で" +
                            "部活やサークルなどの学生団体にパフォーマンスを行ってもらいます。\n" +
                            "ダンスや吹奏楽、マジックなど様々な演技があります。ぜひ見に来てください!!"
                ),
                EventContent(
                    "出演団体", text = ""
                ),
                EventContent(
                    "タイムテーブル", text =
                    "1. \t10:00~10:03\tオープニング\n" +
                            "9. \t13:43~13:45\tエンディング"
                )
            )
        ),
        Event(
            name = "Nitech25",
            img = null,
            date = "11/20(日) 14:00 ~ 15:00",
            location = "2号館前ステージ",
            locationId = LocationId.STAGE,
            latLngList = mapOf("2号館前ステージ" to CommonLatLng.stage),
            description = listOf(
                EventContent(
                    title = "企画内容",
                    text = "クイズゲーム。昼戦会の延長企画！パネルクイズゲームで優勝して豪華賞品を手に入れよう！",
                )
            )
        ),
        Event(
            name = "研究室見学",
            img = null,
            date = "11/19(土) 10:00 ~ 16:00\n11/20(日) 10:00 ~ 15:00",
            location = "52号館5214・5216講義室",
            locationId = LocationId.FIFTYTWO,
            latLngList = mapOf("52号館5214・5216講義室" to CommonLatLng.fiftyTwo),
            description = listOf(
                EventContent(
                    "企画内容",
                    text = "名古屋工業大学の研究室をいくつかピックアップし、そこで行われている研究内容を展示します。"
                ),
                EventContent(
                    title = "注意事項", text = "展示物にお手を触れないようにお願いします。"
                )
            )
        ),
        Event(
            name = "古墳マンのブラ歩き",
            img = null,
            date = "11/19(土) 10:00 ~ 16:00\n11/20(日) 10:00 ~ 15:30",
            location = "2号館前、模擬店前、52号館前、正門",
            locationId = null,
            latLngList = mapOf(
                "2号館前" to CommonLatLng.stage,
                "模擬店前" to CommonLatLng.fourSouth,
                "正門" to CommonLatLng.seimon
            ),
            description = listOf(
                EventContent(
                    title = "企画内容", text = "1. 学校内をたまごちゃん、古墳マンが歩き回る\n" +
                            "2. バルーンアートをプレゼントする\n" +
                            "3. 記念写真を撮影する"
                )
            )
        ),
        Event(
            name = "後夜祭",
            img = null,
            date = "11/20(日) 15:30 ~ 18:00",
            location = "2号館前ステージ",
            locationId = LocationId.STAGE,
            latLngList = mapOf(
                "2号館前ステージ" to CommonLatLng.stage,
                "整理券配布場所 (晴天)" to CommonLatLng.ticket,
                "整理券配布場所 (雨天)" to CommonLatLng.nitechHallFront
            ),
            description = listOf(
                EventContent(
                    title = "入場制限", text = "観覧エリアに入場するには整理券が必要です."
                ),
                EventContent(
                    subtitle = "配布場所 (晴天時)", img = R.drawable.haifu
                ),
                EventContent(
                    subtitle = "配布場所 (雨天)", text = "NITech Hall前"
                ),
                EventContent(
                    subtitle = "配布時間", text = "・11:00 ~\n" +
                            "・13:00 ~\n" +
                            "※ 各時間150枚ずつ配布し、なくなり次第終了します。"
                ),
                EventContent(
                    title = "企画内容", text = "第60回工大祭最後のステージ企画となります！" +
                            "【楽しむか、盛り上がるか、どっちなんだい、パワー！？】\n" +
                            "1. 抽選会 豪華景品が当たるチャンス！たくさんの参加お待ちしています\n" +
                            "2. 名工ダンス 楽しく踊って盛り上がろう！前夜祭でフリ動画もみることができるので、" +
                            "ぜひ覚えてきてください\n"
                )
            )
        ),
        Event(
            name = "古墳マンのわくわくおもちゃ工房",
            img = null,
            date = "11/19(土) 10:00 ~ 16:00\n11/20(日) 10:00 ~ 15:30",
            location = "52号館1階ゆめルーム",
            locationId = LocationId.FIFTYTWO,
            latLngList = mapOf("52号館1階ゆめルーム" to CommonLatLng.fiftyTwo),
            description = listOf(
                EventContent(
                    title = "企画内容",
                    text = "1. スライム作り\n" +
                            "2. スノードーム作り\n" +
                            "3. ミニクリスマスツリー作り",
                ),
                EventContent(
                    title = "注意事項",
                    text = "未就学児の方のご参加は保護者の同伴が必要です。年齢制限はありません。"
                )
            )
        ),
        Event(
            name = "昼戦会",
            img = null,
            date = "11/20(日) 11:00 ~ 13:30",
            location = "2号館前ステージ",
            locationId = LocationId.STAGE,
            latLngList = mapOf("2号館前ステージ" to CommonLatLng.stage),
            description = listOf(
                EventContent(
                    title = "企画内容",
                    text = "工大祭ステージの新企画！ミニゲームと抽選会を行い、" +
                            "優勝者・当選者には豪華景品を贈呈します！景品獲得を目指してミニゲームを楽しもう！",
                )
            )
        ),
        Event(
            name = "謎解き",
            img = null,
            date = "11/19(土) 10:00 ~ 15:30\n11/20(日) 10:00 ~ 15:30",
            location = "52号館5224講義室",
            locationId = LocationId.FIFTYTWO,
            latLngList = mapOf("52号館5224講義室" to CommonLatLng.fiftyTwo),
            description = listOf(
                EventContent(
                    "企画内容",
                    text = "名工大の講義室から2人 ~ 4人のグループで脱出を目指す脱出ゲームを行います。"
                ),
                EventContent(
                    title = "注意事項", text = "全年齢対象(ただし小学生以下は保護者の同伴を推奨)\n" +
                            "上記の開催場所にて先着順に整理券配布\n" +
                            "募集は13:30まで\n" +
                            "1人 ~ 3人のグループは他の組と合併する可能性あり"
                )
            )
        ),
        Event(
            name = "名工トライアスロン",
            img = null,
            date = "11/19(土) 10:00 ~ 16:00\n11/20(日) 10:00 ~ 16:00",
            location = "柔道場",
            locationId = LocationId.GYM,
            latLngList = mapOf("柔道場" to CommonLatLng.judoGym),
            description = listOf(
                EventContent(
                    "企画内容", text = "運、知能、体力の3競技の結果を特典かしてその総合点を" +
                            "競ってもらい、ランキングの順位に応じて景品を与える。"
                ),
                EventContent(
                    subtitle = "競技", text = "1. 知能: 絵を短時間で記憶するゲーム\n" +
                            "2. 体力: ストラックアウト\n3. 運: サイコロ\""
                ),
                EventContent(
                    title = "お知らせ", text = "ランキングにのると景品あり",
                ),
                EventContent(
                    title = "注意事項", text = "最大2人1組です。",
                )
            )
        ),
        Event(
            name = "模擬ワングランプリ",
            img = null,
            date = "11/19(土) 10:00 ~ 15:30\n11/20(日) 10:00 ~ 15:30\n表彰式 11/20(日) 15:30 ~",
            location = "4号館南\n" +
                    "2号館ステージ前",
            locationId = LocationId.STAGE,
            latLngList = mapOf(
                "4号館南" to CommonLatLng.fourSouth,
                "2号館ステージ前" to CommonLatLng.stage
            ),
            description = listOf(
                EventContent(
                    "企画内容",
                    text = "模擬店の来訪者に1番良かった模擬店を投票してもらい上位3店舗を表彰します"
                ),
                EventContent(
                    title = "注意事項",
                    text = "未就学児の方のご参加は保護者の同伴が必要です。年齢制限はありません。",
                )
            )
        ),
    )

}