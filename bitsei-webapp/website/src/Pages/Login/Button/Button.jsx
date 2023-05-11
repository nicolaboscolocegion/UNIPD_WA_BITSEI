import React from "react";
import {useSelector} from "react-redux";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {faSpinner} from '@fortawesome/free-solid-svg-icons'

function Button() {
    const pending = useSelector((state) => state.auth.pending);

    return (
        <div className="pt-1 mb-4">
            <button
                className="btn btn-info btn-lg btn-block"
                type="submit"
            >
                {pending ? <FontAwesomeIcon icon={faSpinner} color="blue" spin/> : ("Login")}
            </button>
        </div>

    );
}

export default Button;
