import React, {useState} from 'react';
import './styles/SVG.css';

const SVGpart = ({ 
    svgContent,
    functions,
    selectedFunction,
    setSelectedFunction,
    llContent, 
    onGetRequest, 
    selectedOption,
    infoContent,
    onBlockClick
}) => {

    const [blockTitle, setBlockTitle] = useState('');
    const [popupVisible, setPopupVisible] = useState(false);
    const [popupPosition, setPopupPosition] = useState({ x: 0, y: 0 });
    
    const handleSvgClick = (event) => {
        const node = event.target.closest('.node');
        const edge = event.target.closest('.edge');

        if (node) {
            const title = node.querySelector('title')?.textContent || 'Без названия';
            setBlockTitle(title);
            console.log("Информация о блоке:", blockTitle); // Выводим информацию о блоке в консоль
            onBlockClick(blockTitle);
            if (selectedOption === "LoopsInfo") {
                setPopupPosition({ x: event.clientX, y: event.clientY });
                setPopupVisible(true);
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
            {svgContent ? (
                <div>
                    <div className="svg-win"
                        onClick={handleSvgClick}
                        dangerouslySetInnerHTML={{ __html: svgContent }}
                        style={{cursor: "pointer"}}
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
                <div className="svg-placeholder" style={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    width: '100%',
                    height: '300px',
                    border: '2px dashed #ccc',
                    color: '#888',
                    fontSize: '16px'
                }}>
                    Здесть будет CFG-граф.
                </div>
            )}
        </div>
    );
};

export default SVGpart;
