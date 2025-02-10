import React, { useState } from 'react';
import "./styles/OptTree.css"; // Подключаем стили

const OptTree = ({ isOpen, onClose }) => {
  const [selectedOption, setSelectedOption] = useState('');

  const handleDropdownChange = (event) => {
      setSelectedOption(event.target.value);// Обновляем выбранное значение
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
        <div className="projects">
            <select
                value={selectedOption}
                onChange={handleDropdownChange}
                className="dropdown"
            >
                <option value="start">Проекты</option>
                <option disabled>Нет доступных проектов</option>
            </select>
        </div>
        <div className="modal-content">
            <h2 className='title'>Заголовок</h2>
            <button onClick={onClose} className="close-btn">
            X
            </button>
      </div>
    </div>
  );
};

export default OptTree;
