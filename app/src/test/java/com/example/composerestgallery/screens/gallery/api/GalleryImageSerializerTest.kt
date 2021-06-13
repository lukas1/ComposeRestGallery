package com.example.composerestgallery.screens.gallery.api

import com.example.composerestgallery.screens.gallery.viewmodel.GalleryImage
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*

import org.junit.Test

class GalleryImageSerializerTest {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Test
    fun deserialize() {
        val parsedImages = json.decodeFromString<List<GalleryImage>>(inputJson)
        assertEquals(
            listOf(
                GalleryImage(
                    url = "https://images.unsplash.com/photo-1603993097397-89c963e325c7?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MXwxfGFsbHwxfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=1080",
                    description = null,
                    userName = "Jakob Owens"
                ),
                GalleryImage(
                    url = "https://images.unsplash.com/photo-1623562232356-ce84655cd072?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MHwxfGFsbHwyfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=1080",
                    description = null,
                    userName = "Joseph Menjivar"
                ),
                GalleryImage(
                    url = "https://images.unsplash.com/photo-1623555635066-ba30085bc92a?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MHwxfGFsbHwzfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=1080",
                    description = "some description",
                    userName = "Ryunosuke Kikuno"
                ),
            ),
            parsedImages
        )
    }

    @Test
    fun deserializeInvalidJsonThrowsException() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<GalleryImage>("""
                {
                    "user": {
                        "test": "test"
                    }
                }
            """.trimIndent())
        }
    }

    @Test
    fun serialize() {
        // method is deliberately not implemented, using TODO() method so this is expected
        assertThrows(NotImplementedError::class.java) {
            json.encodeToString(
                listOf(
                    GalleryImage(url = "", description = null, userName = "")
                )
            )
        }
    }
}

private val inputJson = """
[{
	"id": "CTflmHHVrBM",
	"created_at": "2020-10-29T13:43:18-04:00",
	"updated_at": "2021-06-12T10:25:58-04:00",
	"promoted_at": "2020-10-29T16:27:01-04:00",
	"width": 6720,
	"height": 4480,
	"color": "#400c40",
	"blur_hash": "LQD9Pg_N1D4npLtRxst7ENWq-p%M",
	"description": null,
	"alt_description": "man in black jacket holding camera",
	"urls": {
		"raw": "https://images.unsplash.com/photo-1603993097397-89c963e325c7?ixid=MnwyMzg2MzF8MXwxfGFsbHwxfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1",
		"full": "https://images.unsplash.com/photo-1603993097397-89c963e325c7?crop=entropy\u0026cs=srgb\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MXwxfGFsbHwxfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=85",
		"regular": "https://images.unsplash.com/photo-1603993097397-89c963e325c7?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MXwxfGFsbHwxfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=1080",
		"small": "https://images.unsplash.com/photo-1603993097397-89c963e325c7?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MXwxfGFsbHwxfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=400",
		"thumb": "https://images.unsplash.com/photo-1603993097397-89c963e325c7?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MXwxfGFsbHwxfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=200"
	},
	"links": {
		"self": "https://api.unsplash.com/photos/CTflmHHVrBM",
		"html": "https://unsplash.com/photos/CTflmHHVrBM",
		"download": "https://unsplash.com/photos/CTflmHHVrBM/download",
		"download_location": "https://api.unsplash.com/photos/CTflmHHVrBM/download?ixid=MnwyMzg2MzF8MXwxfGFsbHwxfHx8fHx8Mnx8MTYyMzU3MzQ1NQ"
	},
	"categories": [],
	"likes": 1000,
	"liked_by_user": false,
	"current_user_collections": [],
	"sponsorship": {
		"impression_urls": ["https://ad.doubleclick.net/ddm/trackimp/N1224323.3286893UNSPLASH/B25600467.299679141;dc_trk_aid=492448064;dc_trk_cid=148386287;ord=[timestamp];dc_lat=;dc_rdid=;tag_for_child_directed_treatment=;tfua=;ltd=?", "https://secure.insightexpressai.com/adServer/adServerESI.aspx?script=false\u0026bannerID=8614729\u0026rnd=[timestamp]\u0026redir=https://secure.insightexpressai.com/adserver/1pixel.gif", "https://tag.researchnow.com/t/beacon?pr=285833\u0026adn=1\u0026ca=25600467\u0026si=6303199\u0026pl=299679141\u0026cr=148386287\u0026did=137\u0026ord=[timestamp]\u0026gdpr=GDPR}\u0026gdpr_consent=GDPR_CONSENT_110}\u0026us_privacy=US_PRIVACY}"],
		"tagline": "For Growing Businesses",
		"tagline_url": "https://ad.doubleclick.net/ddm/trackclk/N1224323.3286893UNSPLASH/B25600467.299679141;dc_trk_aid=492448064;dc_trk_cid=148386287;dc_lat=;dc_rdid=;tag_for_child_directed_treatment=;tfua=;ltd=",
		"sponsor": {
			"id": "D-bxv1Imc-o",
			"updated_at": "2021-06-12T13:42:25-04:00",
			"username": "mailchimp",
			"name": "Mailchimp",
			"first_name": "Mailchimp",
			"last_name": null,
			"twitter_username": "Mailchimp",
			"portfolio_url": "https://mailchimp.com/",
			"bio": "The all-in-one Marketing Platform built for growing businesses.",
			"location": null,
			"links": {
				"self": "https://api.unsplash.com/users/mailchimp",
				"html": "https://unsplash.com/@mailchimp",
				"photos": "https://api.unsplash.com/users/mailchimp/photos",
				"likes": "https://api.unsplash.com/users/mailchimp/likes",
				"portfolio": "https://api.unsplash.com/users/mailchimp/portfolio",
				"following": "https://api.unsplash.com/users/mailchimp/following",
				"followers": "https://api.unsplash.com/users/mailchimp/followers"
			},
			"profile_image": {
				"small": "https://images.unsplash.com/profile-1609545740442-928866556c38image?ixlib=rb-1.2.1\u0026q=80\u0026fm=jpg\u0026crop=faces\u0026cs=tinysrgb\u0026fit=crop\u0026h=32\u0026w=32",
				"medium": "https://images.unsplash.com/profile-1609545740442-928866556c38image?ixlib=rb-1.2.1\u0026q=80\u0026fm=jpg\u0026crop=faces\u0026cs=tinysrgb\u0026fit=crop\u0026h=64\u0026w=64",
				"large": "https://images.unsplash.com/profile-1609545740442-928866556c38image?ixlib=rb-1.2.1\u0026q=80\u0026fm=jpg\u0026crop=faces\u0026cs=tinysrgb\u0026fit=crop\u0026h=128\u0026w=128"
			},
			"instagram_username": "mailchimp",
			"total_collections": 0,
			"total_likes": 11,
			"total_photos": 0,
			"accepted_tos": false,
			"for_hire": false
		}
	},
	"user": {
		"id": "1Ii2-3J-e_o",
		"updated_at": "2021-06-13T03:32:30-04:00",
		"username": "jakobowens1",
		"name": "Jakob Owens",
		"first_name": "Jakob",
		"last_name": "Owens",
		"twitter_username": "jakobOwenss",
		"portfolio_url": "http://www.directorjakobowens.com",
		"bio": "Filmmaker and Photographer: LA/PHX - Instagram: @JakobOwens\r\nLightroom Presets available here: http://bit.ly/2nzXy7z",
		"location": null,
		"links": {
			"self": "https://api.unsplash.com/users/jakobowens1",
			"html": "https://unsplash.com/@jakobowens1",
			"photos": "https://api.unsplash.com/users/jakobowens1/photos",
			"likes": "https://api.unsplash.com/users/jakobowens1/likes",
			"portfolio": "https://api.unsplash.com/users/jakobowens1/portfolio",
			"following": "https://api.unsplash.com/users/jakobowens1/following",
			"followers": "https://api.unsplash.com/users/jakobowens1/followers"
		},
		"profile_image": {
			"small": "https://images.unsplash.com/profile-1489915140304-be21c5eb4986?ixlib=rb-1.2.1\u0026q=80\u0026fm=jpg\u0026crop=faces\u0026cs=tinysrgb\u0026fit=crop\u0026h=32\u0026w=32",
			"medium": "https://images.unsplash.com/profile-1489915140304-be21c5eb4986?ixlib=rb-1.2.1\u0026q=80\u0026fm=jpg\u0026crop=faces\u0026cs=tinysrgb\u0026fit=crop\u0026h=64\u0026w=64",
			"large": "https://images.unsplash.com/profile-1489915140304-be21c5eb4986?ixlib=rb-1.2.1\u0026q=80\u0026fm=jpg\u0026crop=faces\u0026cs=tinysrgb\u0026fit=crop\u0026h=128\u0026w=128"
		},
		"instagram_username": "jakobowens",
		"total_collections": 2,
		"total_likes": 444,
		"total_photos": 1007,
		"accepted_tos": true,
		"for_hire": true
	}
}, {
	"id": "9Ri0F9twt9Y",
	"created_at": "2021-06-13T01:30:36-04:00",
	"updated_at": "2021-06-13T04:36:02-04:00",
	"promoted_at": "2021-06-13T04:36:02-04:00",
	"width": 4228,
	"height": 6342,
	"color": "#262626",
	"blur_hash": "LQDcH^n1NGNepfVrs+R-MzSikCoJ",
	"description": "",
	"alt_description": null,
	"urls": {
		"raw": "https://images.unsplash.com/photo-1623562232356-ce84655cd072?ixid=MnwyMzg2MzF8MHwxfGFsbHwyfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1",
		"full": "https://images.unsplash.com/photo-1623562232356-ce84655cd072?crop=entropy\u0026cs=srgb\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MHwxfGFsbHwyfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=85",
		"regular": "https://images.unsplash.com/photo-1623562232356-ce84655cd072?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MHwxfGFsbHwyfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=1080",
		"small": "https://images.unsplash.com/photo-1623562232356-ce84655cd072?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MHwxfGFsbHwyfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=400",
		"thumb": "https://images.unsplash.com/photo-1623562232356-ce84655cd072?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MHwxfGFsbHwyfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=200"
	},
	"links": {
		"self": "https://api.unsplash.com/photos/9Ri0F9twt9Y",
		"html": "https://unsplash.com/photos/9Ri0F9twt9Y",
		"download": "https://unsplash.com/photos/9Ri0F9twt9Y/download",
		"download_location": "https://api.unsplash.com/photos/9Ri0F9twt9Y/download?ixid=MnwyMzg2MzF8MHwxfGFsbHwyfHx8fHx8Mnx8MTYyMzU3MzQ1NQ"
	},
	"categories": [],
	"likes": 0,
	"liked_by_user": false,
	"current_user_collections": [],
	"sponsorship": null,
	"user": {
		"id": "yH1haCOCs4c",
		"updated_at": "2021-06-13T04:36:02-04:00",
		"username": "josephm82",
		"name": "Joseph Menjivar",
		"first_name": "Joseph",
		"last_name": "Menjivar",
		"twitter_username": null,
		"portfolio_url": null,
		"bio": null,
		"location": "Los Angeles, California ",
		"links": {
			"self": "https://api.unsplash.com/users/josephm82",
			"html": "https://unsplash.com/@josephm82",
			"photos": "https://api.unsplash.com/users/josephm82/photos",
			"likes": "https://api.unsplash.com/users/josephm82/likes",
			"portfolio": "https://api.unsplash.com/users/josephm82/portfolio",
			"following": "https://api.unsplash.com/users/josephm82/following",
			"followers": "https://api.unsplash.com/users/josephm82/followers"
		},
		"profile_image": {
			"small": "https://images.unsplash.com/profile-1595807689014-3d67cc37706eimage?ixlib=rb-1.2.1\u0026q=80\u0026fm=jpg\u0026crop=faces\u0026cs=tinysrgb\u0026fit=crop\u0026h=32\u0026w=32",
			"medium": "https://images.unsplash.com/profile-1595807689014-3d67cc37706eimage?ixlib=rb-1.2.1\u0026q=80\u0026fm=jpg\u0026crop=faces\u0026cs=tinysrgb\u0026fit=crop\u0026h=64\u0026w=64",
			"large": "https://images.unsplash.com/profile-1595807689014-3d67cc37706eimage?ixlib=rb-1.2.1\u0026q=80\u0026fm=jpg\u0026crop=faces\u0026cs=tinysrgb\u0026fit=crop\u0026h=128\u0026w=128"
		},
		"instagram_username": "Jmenjivarpix",
		"total_collections": 0,
		"total_likes": 1182,
		"total_photos": 67,
		"accepted_tos": true,
		"for_hire": true
	}
}, {
	"id": "1mXIjSmc8Pw",
	"created_at": "2021-06-12T23:40:54-04:00",
	"updated_at": "2021-06-13T04:35:31-04:00",
	"promoted_at": "2021-06-13T04:33:02-04:00",
	"width": 4024,
	"height": 6048,
	"color": "#f3c00c",
	"blur_hash": "LuKw]${'$'}0;mR:hJCs8niofaeW=WobG",
	"description": "some description",
	"alt_description": null,
	"urls": {
		"raw": "https://images.unsplash.com/photo-1623555635066-ba30085bc92a?ixid=MnwyMzg2MzF8MHwxfGFsbHwzfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1",
		"full": "https://images.unsplash.com/photo-1623555635066-ba30085bc92a?crop=entropy\u0026cs=srgb\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MHwxfGFsbHwzfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=85",
		"regular": "https://images.unsplash.com/photo-1623555635066-ba30085bc92a?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MHwxfGFsbHwzfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=1080",
		"small": "https://images.unsplash.com/photo-1623555635066-ba30085bc92a?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MHwxfGFsbHwzfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=400",
		"thumb": "https://images.unsplash.com/photo-1623555635066-ba30085bc92a?crop=entropy\u0026cs=tinysrgb\u0026fit=max\u0026fm=jpg\u0026ixid=MnwyMzg2MzF8MHwxfGFsbHwzfHx8fHx8Mnx8MTYyMzU3MzQ1NQ\u0026ixlib=rb-1.2.1\u0026q=80\u0026w=200"
	},
	"links": {
		"self": "https://api.unsplash.com/photos/1mXIjSmc8Pw",
		"html": "https://unsplash.com/photos/1mXIjSmc8Pw",
		"download": "https://unsplash.com/photos/1mXIjSmc8Pw/download",
		"download_location": "https://api.unsplash.com/photos/1mXIjSmc8Pw/download?ixid=MnwyMzg2MzF8MHwxfGFsbHwzfHx8fHx8Mnx8MTYyMzU3MzQ1NQ"
	},
	"categories": [],
	"likes": 0,
	"liked_by_user": false,
	"current_user_collections": [],
	"sponsorship": null,
	"user": {
		"id": "lHGLHMCyqs8",
		"updated_at": "2021-06-13T04:33:02-04:00",
		"username": "ryunosuke_kikuno",
		"name": "Ryunosuke Kikuno",
		"first_name": "Ryunosuke",
		"last_name": "Kikuno",
		"twitter_username": "ryunosuke_kkn",
		"portfolio_url": "https://www.instagram.com/ryunosuke_kikuno/",
		"bio": "Tokyo, Japan ⇨ Canada",
		"location": "Calgary, Canada",
		"links": {
			"self": "https://api.unsplash.com/users/ryunosuke_kikuno",
			"html": "https://unsplash.com/@ryunosuke_kikuno",
			"photos": "https://api.unsplash.com/users/ryunosuke_kikuno/photos",
			"likes": "https://api.unsplash.com/users/ryunosuke_kikuno/likes",
			"portfolio": "https://api.unsplash.com/users/ryunosuke_kikuno/portfolio",
			"following": "https://api.unsplash.com/users/ryunosuke_kikuno/following",
			"followers": "https://api.unsplash.com/users/ryunosuke_kikuno/followers"
		},
		"profile_image": {
			"small": "https://images.unsplash.com/profile-1621479987012-73ba1c118855image?ixlib=rb-1.2.1\u0026q=80\u0026fm=jpg\u0026crop=faces\u0026cs=tinysrgb\u0026fit=crop\u0026h=32\u0026w=32",
			"medium": "https://images.unsplash.com/profile-1621479987012-73ba1c118855image?ixlib=rb-1.2.1\u0026q=80\u0026fm=jpg\u0026crop=faces\u0026cs=tinysrgb\u0026fit=crop\u0026h=64\u0026w=64",
			"large": "https://images.unsplash.com/profile-1621479987012-73ba1c118855image?ixlib=rb-1.2.1\u0026q=80\u0026fm=jpg\u0026crop=faces\u0026cs=tinysrgb\u0026fit=crop\u0026h=128\u0026w=128"
		},
		"instagram_username": "ryunosuke_kikuno",
		"total_collections": 5,
		"total_likes": 771,
		"total_photos": 1049,
		"accepted_tos": true,
		"for_hire": true
	}
}]
"""