package ikube.security;

import org.apache.commons.httpclient.HttpClient;

/**
 * TODO Document me...
 * 
 * @author Michael Couck
 * @since 12.04.2013
 * @version 01.00
 */
public class KerberosAuthentication implements IAuthentication {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void authenticate(HttpClient httpClient, String... properties) {
		// Negotiate
		// YIIIVgYGKwYBBQUCoIIISjCCCEagDTALBgkqhkiG9xIBAgKhBAMCATCigggtBIIIKWCCCCUGCSqGSIb3EgECAgEAboIIFDCCCBCgAwIBBaEDAgEOogcDBQAAAAAAo4IHNGGCBzAwggcsoAMCAQWhERsPUE9TVC5
		// CUEdORVQuTkVUoiIwIKADAgEAoRkwFxsESFRUUBsPc2RsLWR2Mi5uZXRwb3N0o4IG7DCCBuigAwIBF6EDAgECooIG2gSCBtbWVq3Y2jglz2L+TZNnEN5L0JEO15m5bOMCtcSSM+0ffZTl8/bn7A8kjTYI/m6v25lYU
		// ol+w7UKQaCsM0DteTsJF04wcIy8nqQDALXIienTyTveZ2FP8i6Az1N3QOmnlWyR5ruFbltXFqYqJ/jQ0BBLiLC4vHH7uMvr18ulea+IU01cZQA2qcNd1CCoJAnWmozpGlWrr5qvX2mggBaD89U8Z51D3lu0VqKrwzL4Qxv
		// 3cfe9bb2aP/Liml4tgRFFaux/iBiKG9rLNqXsAJdUTKAThLzP1eWJwxt1kHPgMyzEADuoI3XTE3EYSrdMjRO7PH4eeVfUoGrWIUG2L8AubKgXT5buijmkbb5NyMhVKRaxWcGHCyZDGt1BuPm4IDFq77MsFEJxMfXI1P5YE
		// xAiLDAQQdbXpB8tmzN2FxkIAdfmqn9pul1CYt7RiTUl37H1wusA6Uf6O8Pg4DLCUlD2tWwnX+okLBJGrZdRPQMLi4RrmNUjZOF4mRG5a5Wck02xrtlVfbPcI8Uw9IMwBteffWsX6lm2gM2hlLdsPm2WFzPB9/MkpyRuZC
		// GFCvNIYQs03iPu55MMRuGSd8s7SzB3SmEUMF6I1oMm/0WKtD9VjaJHujf9c3QYp3Xo70ejLUPXOtiKuCEXlsi/ou5+drDKYvx7Bln4uPn2wg2/EjWBwE0thdinU5DGyXZO58MH5E1RdGevDgq3cYsUNLcl+xbZNQWQLo
		// SYVnq35Rnfb4F2ZHqiKTP0+EPS2IFykv1GEH4pT1YThgtHacW/Gfd3aHhE/emVBWUHr59NqQIFjgBROTneiOP4G2IwEpSqNLKiSaBbTj3yiFq/ttkz4jKR3ct+sAqwGuRw4Y9oRAYrVcjYbpA8Q8dZ32CknK0eN9pc0O4
		// qSW+H9EBodmC+C3NZB3NqSwU/veNJ88g5DkB1XtPLkkMFWQEvVItcALWNLb4uAPBJSVaR0rZ/J2V3ooQT0fyoTjv5qLIDC1IPcQiijg0B2Bx60kRuPvmf1cQJp06RXNiTEtMheINEeWqMVTLAkT9pJoOqtsPtyPF8Hv
		// YV3Hxa2cKOz8d1B6DrAn0F6EHeGzyxqfmqbqmSibI80zf+UlB8THSEy6GvqBR5I4LdEIFjr6D7AUSjXNyyNuCyH1O+o7EEeWXf49LdwEmpIEvs0v0rqs+Hml7ztHDrtrYpz6aEcg5VUh350jqqriOM2dVxDSvxisQsPtcczGN
		// G6RWBmPlaqJ+2Lw8UOGjWJg1L1IWpjwi0giQZ+JHa6Qj7kAS7SxPsciuLRfmekWivvGujyp5X9KogJ2gAAf6cUlCwGfd2BO0sJOTSlk/e0oYhN0P7LWgeH4FBsRqa3pvpn2x5lh//bFlBoP0Zz7YAcBf8xCxfQuiePwhRqZcb
		// 85Cw1Kr5mqxZCSsaqiIrcD79JnXX/KegxD7tt3R7mISsZk/3bHDDU6iaJZRWtxs5xLWjIrnYOLldaYlTo1MxJUd7MHM5lbjw5cht4YnSV6njv8T6Q6oh3mPUSR1izYb3BCBMFqqv+OgSiWLylbxZkxdtf2lhTg5trWDCqbT91l
		// 9hPGOqWQSl7ek33GApjN+RynU04vYOrhJYEwwgPoxBAE2IeMVYZ9uITY7F9vTrhdEVt27HmqHBTiXezL1vcKJS+uwF0hGQ3Uq/PtZhwrUMdNiQPbJ7K+cDVzMQFyN6ea7BRMJX3EJppZvajD4MFffOPblv0+qP0mxC
		// SJaf++XQxOyOGwm7+YskjdZ3rti4b6OfK/atP0vUg36pUhyDRGLmCigp8eVQoO7OMUj39iRd++gQagVCxQbl1UUu55krq+K512baUykhrdfTq2/ex4pgv3FK3KolSS4heQpWA6FqPmuX59F0TaYy425eKMiTMuOFIj/bM
		// RFnllTQKdbDXrdxis8EU5tL+OLGu+1purioTxBs0Pk6hmtsavQu07K+lvPUE9lj0VUChzaSgpdVOVRcy0AMHHkr2VKERVJkcBgxvNngp0hwof0C2kpuShFb/43KZuo4ZxTpUPPw067/wA2liX4cqxQktjrj9NpFIGdGUVvq2HJ
		// Ob+EyL4xHkCE+damdnnfrmSgEdHdQ/s6f/aT/or0U3+y+cKBrce6vbtNQ9ZrRgpmeAUxxIG/dTp+HkgTGOIcRq3BWEn7HupHUZk3uWXYocjb7rW/ZFkj7FcuJhQn6NXCRxDQ9VoGMMJ/hTieVBHZ4Qbc1exnSos1eed
		// 3p5lQfoJpxgt8IgGbvBdif9RruWg5GHUmYTYBLHd5CLNVhkwQjAfTTl/2jK85bkCQBmoPnwSNySbeUbeH0NZANInMydN4GdskMng7nabYKkrCKpIHCMIG/oAMCAReigbcEgbQLmcAf+JQ7akaWJ+pYYzMTaaYvtlDsU1
		// mc1L7QKHQrERHjfXWD8xCBn/1yu9GzmoG0DLJkkL+k3NUNNKCfGa3Oc9GqnPhsr6sM9XsY/kXGnEfaVGbAtzP+KtXnMXnEcKm+70fc1b8ja0ADm5HYNlIsVRHP1MrzrWJoZpTSUCzSyXGC3+viID6sRRlGzXvBG2IAP+nI
		// AoJSwXYymWmFHsA3gaRu7lv5TP/0+VMJbKknQSZIZ3U=
	}

}
