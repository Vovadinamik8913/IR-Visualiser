import React, {useState, useRef} from 'react';
import './styles/SVG.css';

let clickCounter = 0;

const SVGpart = ({ 
    svgContent,
    functions,
    selectedFunction,
    setSelectedFunction,
    llContent, 
    onGetRequest, 
    selectedOption,
    infoContent,
    onBlockClick,
    howManyClicks,
}) => {

    const [blockTitle, setBlockTitle] = useState('');
    const [blockNumber, setBlockNumber] = useState('');
    const [popupVisible, setPopupVisible] = useState(false);
    const [popupPosition, setPopupPosition] = useState({ x: 0, y: 0 });
    const [scale, setScale] = useState(1);
    const svgContainerRef = useRef(null);
    
    const handleSvgClick = (event) => {
        const node = event.target.closest('.node');
        const edge = event.target.closest('.edge');

        if (node) {
            const title = node.querySelector('title')?.textContent || 'Без названия';
            if(blockTitle === title) {
                clickCounter = clickCounter + 1 > 3 ? 1 : clickCounter + 1;
                console.log(clickCounter);
            } else {
                clickCounter = 1;
                console.log(clickCounter);
            }
            setBlockTitle(title);
            const textElement = node.querySelector('text');
            const number = textElement?.textContent.trim().replace(':', '') || 'Нет данных';
            setBlockNumber(number);
            console.log("Информация о блоке:", blockTitle); // Выводим информацию о блоке в консоль
            onBlockClick(blockNumber, blockTitle);
            if (selectedOption === "LoopsInfo" || selectedOption === "Scev") {
                if(event.ctrlKey) {
                    setPopupVisible(true);
                    setPopupPosition({ x: event.clientX, y: event.clientY });
                }
                else { 
                    setPopupVisible(false);
                }
                howManyClicks(clickCounter);
            }
        } else if (edge) {
            const blockFrom = edge.querySelector('title')?.textContent.split("->")[0] || 'NO NAME';
            console.log("Ребро из блока:", blockFrom);
            const blockTo = edge.querySelector('title')?.textContent.split('->')[1] || 'NO NAME';
            console.log("в блок:", blockTo);
            setPopupVisible(false);
        } else {
            console.log("Клик вне блоков и рёбер");
            setPopupVisible(false);
        }
    };

    const handleDropdownChange = (event) => {
        setSelectedFunction(event.target.value);// Обновляем выбранное значение
        onGetRequest(event.target.value);
    };

    const handleSliderChange = (event) => {
        setScale(event.target.value);
    };

    
    return (
        <div className="window">
            { llContent &&
            <div>
                <select
                value={selectedFunction}
                onChange={handleDropdownChange}
                className="dropdown"
                >
                <option value="start">Выберите функцию</option>
                {functions.length > 0 ? (
                    functions.map((func, index) => (
                        <option key={index} value={func}>
                            {func}
                        </option>
                    ))
                ) : (
                    <option disabled>Нет доступных функций</option>
                )}
                </select>
            </div>}
            <input 
                type="range" 
                min="0.1" 
                max="1"
                step="0.01" 
                value={scale} 
                onChange={handleSliderChange} 
                className="zoom-slider"
            />
            {svgContent ? (
                <div className="svg-container">
                    <div className="svg-win"
                        ref={svgContainerRef}
                        onClick={handleSvgClick}
                        dangerouslySetInnerHTML={{ __html: `
                            <div style='
                            display: inline-block; 
                            align-items: center;
                            justify-content: center;
                            transform: scale(${scale}); 
                            transform-origin: top left;'>
                              ${svgContent}
                            </div>` }}
                    />
                    {popupVisible && selectedOption === "LoopsInfo" && (
                        <div 
                            className="popup-info" 
                            style={{ top: popupPosition.y, left: popupPosition.x }}
                        >
                            {infoContent || "Нет данных"}
                        </div>
                    )}
                </div>
            ) : (
                <div className="svg-placeholder">
                    Здесь будет CFG.
                </div>
            )}
        </div>
    );
};

export default SVGpart;
