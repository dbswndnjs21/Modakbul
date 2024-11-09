const calendarDiv = document.getElementById('calendar');
const toggleButton = document.getElementById('toggleButton');

let calendar;
let checkInDate = '';
let checkOutDate = '';
let isCheckInSelected = false;
let isCheckOutSelected = false;

document.addEventListener('DOMContentLoaded', function () {
    const calendarEl = document.getElementById('calendar');

    // URL에서 체크인, 체크아웃 날짜 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    checkInDate = urlParams.get('checkInDate') || new Date().toISOString().slice(0, 10); // 오늘 날짜
    checkOutDate = urlParams.get('checkOutDate') || new Date(new Date().setDate(new Date().getDate() + 1)).toISOString().slice(0, 10); // 내일 날짜

    const today = new Date();
    const maxDate = new Date();
    maxDate.setDate(today.getDate() + 92); // 오늘 날짜로부터 92일 후

    // 날짜를 input 요소에 설정
    updateDateInputs();

    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        selectable: true,
        select: handleDateSelect,
        unselectAuto: false
    });

    // 초기 상태에서 체크인 및 체크아웃 날짜 반영
    addCalendarEvents();

    calendar.render();
});

toggleButton.addEventListener('click', function () {
    const isHidden = calendarDiv.style.display === 'none';
    calendarDiv.style.display = isHidden ? 'block' : 'none';
    toggleButton.textContent = isHidden ? '숨기기' : '보이기'; // 버튼 텍스트 변경
    if (isHidden) calendar.render(); // FullCalendar를 다시 렌더링
});

function updateDateInputs() {
    document.getElementById('checkInDate').value = checkInDate;
    document.getElementById('checkOutDate').value = checkOutDate;
    document.getElementById('checkInDisplay').innerText = checkInDate;
    document.getElementById('checkOutDisplay').innerText = checkOutDate;
}

function handleDateSelect(info) {
    const selectedDate = new Date(info.startStr);
    const today = new Date();
    today.setHours(0, 0, 0, 0); // 오늘 날짜의 시간 부분을 0으로 설정

    // 체크인 날짜로 오늘 날짜 및 이후 날짜만 허용
    if (selectedDate < today || selectedDate > new Date().setDate(today.getDate() + 92)) {
        return; // 오늘 이전 날짜 또는 최대 선택 가능 날짜를 초과한 경우
    }

    // 체크인 날짜가 이미 선택된 상태에서 선택된 날짜가 체크인 날짜 이전일 경우
    if (isCheckInSelected && selectedDate < new Date(checkInDate)) {
        // 기존 체크인 날짜를 체크아웃으로 설정
        checkOutDate = checkInDate; // 기존 체크인 날짜를 체크아웃 날짜로 설정
        checkInDate = info.startStr; // 새로 선택된 날짜를 체크인 날짜로 설정
        isCheckOutSelected = true; // 체크아웃이 선택되었음을 표시
        isCheckInSelected = true; // 체크인 날짜가 설정되었음을 유지
    } else {
        // 체크아웃이 선택된 상태인 경우
        if (isCheckOutSelected) {
            checkInDate = info.startStr; // 체크인 날짜를 선택된 날짜로 설정
            checkOutDate = new Date(selectedDate.setDate(selectedDate.getDate() + 1)).toISOString().slice(0, 10); // 체크아웃 날짜를 체크인 날짜 + 1일로 설정
            isCheckOutSelected = false;
        } else {
            // 체크인 날짜가 선택되지 않은 경우
            if (!isCheckInSelected) {
                checkInDate = info.startStr; // 체크인 날짜를 선택된 날짜로 설정
                checkOutDate = new Date(selectedDate.setDate(selectedDate.getDate() + 1)).toISOString().slice(0, 10); // 체크아웃 날짜를 체크인 날짜 + 1일로 설정
                isCheckInSelected = true; // 체크인 날짜가 설정되었음을 표시
            } else if (selectedDate > new Date(checkInDate)) {
                checkOutDate = info.startStr; // 체크아웃 날짜를 선택된 날짜로 설정
                isCheckOutSelected = true; // 체크아웃이 선택되었음을 표시
            }
        }
    }

    updateDateInputs();
    addCalendarEvents();
}

function addCalendarEvents() {
    calendar.getEvents().forEach(event => event.remove());

    if (checkInDate) {
        calendar.addEvent({
            start: checkInDate,
            end: new Date(new Date(checkOutDate).setDate(new Date(checkOutDate).getDate() + 1)).toISOString().slice(0, 10),
            display: 'background',
            classNames: ['selected']
        });
    }

    if (checkInDate && checkOutDate) {
        calendar.addEvent({
            start: new Date(checkInDate),
            end: new Date(checkOutDate),
            display: 'background',
            classNames: ['selected']
        });
    }
}

function validateForm() {
    return true; // 폼을 제출할 수 있도록 true 반환
}

function setSelectedCampsite(campsiteId) {
    document.getElementById('campsiteId').value = campsiteId;
    document.querySelector('form').submit();
}
