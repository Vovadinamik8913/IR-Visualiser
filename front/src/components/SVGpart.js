import React, {useState} from 'react';

const SVGpart = ({ 
    svgContent,
    functions,
    selectedFunction,
    setSelectedFunction,
    llContent, 
    onGetRequest 
}) => {

    

    const handleSvgClick = (event) => {
        const node = event.target.closest('.node');
        const edge = event.target.closest('.edge');

        if (node) {
            const blockTitle = node.querySelector('title')?.textContent || 'Без названия';
            console.log("Информация о блоке:", blockTitle); // Выводим информацию о блоке в консоль
        } else if (edge) {
            const blockFrom = edge.querySelector('title')?.textContent.split("->")[0] || 'NO NAME';
            console.log("Ребро из блока:", blockFrom);
            const blockTo = edge.querySelector('title')?.textContent.split('->')[1] || 'NO NAME';
            console.log("в блок:", blockTo);
        } else {
            console.log("Клик вне блоков и рёбер");
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
                    SVG пока нет
                </div>
            )}
        </div>
    );
};

export default SVGpart;
