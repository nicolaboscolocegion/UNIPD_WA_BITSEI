import React, {useEffect, useState} from "react";


function ImageTest ({ src, alt, fallback }) {
        const [error, setError] = useState(false);

        const onError = () => {
            setError(true);
        };

        return error ? fallback : <img src={src} alt={alt} onError={onError} width='60px' />;
    }
export default ImageTest;
