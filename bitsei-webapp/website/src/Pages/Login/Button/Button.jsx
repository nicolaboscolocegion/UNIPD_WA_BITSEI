import React from "react";
import {useSelector} from "react-redux";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {faSpinner} from '@fortawesome/free-solid-svg-icons'

function Button({title}) {
    const pending = useSelector((state) => state.auth.pending);

    return (
            <button
                className="btn btn-primary"
                type="submit"
            >
                {pending
                        ? <FontAwesomeIcon icon={faSpinner} color="blue" spin/>
                        : title
                }
            </button>

    );
}

export default Button;
