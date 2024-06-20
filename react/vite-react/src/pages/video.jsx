import {useEffect, useState, useRef} from 'react';

export default function VideoPlayer() {
    const [isPlaying, setIsPlaying] = useState(false);
    const videoRef = useRef(null);
    const btnRef = useRef(null);

    function handleClick() {
        const nextIsPlaying = !isPlaying;
        setIsPlaying(nextIsPlaying);
        if (nextIsPlaying)
            videoRef.current.play();
        else
            videoRef.current.pause();
    }
    function handleEnded() {
        setIsPlaying(false);
    }
    function handleOnPlay() {
        setIsPlaying(true);
    }

    useEffect(()=>btnRef.current.focus(), []);
    return (
        <div className="subbody videoplayer">
            <h2>Here is a video from mozilla. I hope it works.</h2>
            <video width="450" ref={videoRef}
                    onEnded={handleEnded} onPlay={handleOnPlay} onPause={handleEnded}>
                <source type="video/mp4"
                src="https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4"/>
            </video>
            <div className="videobutton">
                <button onClick={handleClick} ref={btnRef}>
                    {isPlaying ? 'Pause' : 'Play'}
                </button>
            </div>
        </div>
    )
}
