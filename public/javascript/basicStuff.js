
/**
*
*/

console.log("Test!");
const csrfToken = document.getElementById("csrfToken").value;
const getRoute = document.getElementById("getRoute").value;

function shortenDef(){
    let shortenURL = document.getElementById('shortenURL')
    let url = shortenURL.value

    fetch(getRoute, {
    method: "POST",
    headers: new Headers({'Content-Type': 'application/json', 'Accept': 'application/json',  'Csrf-Token': csrfToken}),

        body: JSON.stringify({"urlString": url})
    }).then(res => {
        console.log("Request completed!" );
        console.log(res)
        return res.json()
    }).then(res=> {
        console.log("The request has reached here")
        console.log(res);
        let shortenedURLResp = document.getElementById('shortenedUrlResp');
        let shortenedUrlLink = document.getElementById('shortenedUrlLink');
        if(res['msg']){
        shortenedURLResp.innerHTML = res['msg']
        shortenedUrlLink.href  = ""
        shortenedUrlLink.text = ""
    }
    else{

        shortenedURLResp.innerHTML = "The shortened URL is: "
        shortenedUrlLink.href  = res['url']
        shortenedUrlLink.text = res['url']
        }
    })

};




