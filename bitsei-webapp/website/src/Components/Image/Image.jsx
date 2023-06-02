import React, {useEffect, useState} from "react";
import gate from "../../gate";
import {Buffer} from 'buffer';
import logo from "../../assets/bitseiLogo";

function Image({id}) {
    const [image, setImage] = useState("");

    useEffect(() => {
        gate
            .getCompanyImage(id)
            .then((response) => {
                // check bytes length to see if the image is empty
                if (response.data.byteLength < 500) {
                    setImage(logo());
                    return;
                }
                setImage(Buffer.from(response.data).toString('base64'));
            })
            .catch(() => {
                setImage(logo());
            });
    }, [id]);

    return (
        <img width="70" className="d-inline-block align-text-top"
            src={`data:image/png;base64, ${image}`}
            alt={"company_logo"}
        />

    );
}

export default Image;
