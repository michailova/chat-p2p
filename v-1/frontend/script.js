const peerConnection = new RTCPeerConnection({
    iceServers: [
        {
            urls: "stun:stun.l.google.com:19302"
        }
    ]
});

const createOfferButton =
    document.getElementById("createOfferButton");

createOfferButton.addEventListener("click", async () => {

    const offer =
        await peerConnection.createOffer();

    await peerConnection.setLocalDescription(offer);

    socket.send(
        "OFFER " +
        JSON.stringify(offer)
    );

    addMessage("Offer отправлен");
});