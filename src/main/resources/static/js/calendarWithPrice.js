const calendarDiv = document.getElementById('calendar');
const toggleButton = document.getElementById('toggleButton');

let calendar;
let checkInDate = '';
let checkOutDate = '';
let isCheckInSelected = false;
let isCheckOutSelected = false;

document.addEventListener('DOMContentLoaded', function () {
    var calendarEl = document.getElementById('calendar');

    // URL에서 체크인, 체크아웃 날짜 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    checkInDate = urlParams.get('checkInDate') || new Date().toISOString().slice(0, 10);
    checkOutDate = urlParams.get('checkOutDate') || new Date(new Date().setDate(new Date().getDate() + 1)).toISOString().slice(0, 10);

    const today = new Date();
    const maxDate = new Date();
    maxDate.setDate(today.getDate() + 92); // 오늘 날짜로부터 92일 후

    // 날짜를 input 요소에 설정
    document.getElementById('checkInDate').value = checkInDate;
    document.getElementById('checkOutDate').value = checkOutDate;

    // 화면에 표시될 날짜 설정
    document.getElementById('checkInDisplay').innerText = formatDate(new Date(checkInDate));
    document.getElementById('checkOutDisplay').innerText = formatDate(new Date(checkOutDate));

    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        selectable: true,
        select: function (info) {
            var startDate = new Date(info.startStr);
            if (startDate < today || startDate > maxDate) {
                return;
            }

            // 체크아웃이 선택된 상태일 때 새로운 체크인 날짜를 선택하도록 설정
            if (isCheckOutSelected) {
                checkInDate = startDate;
                checkOutDate = new Date(new Date(checkInDate).setDate(new Date(checkInDate).getDate() + 1)).toISOString().slice(0, 10);
                isCheckOutSelected = false;
            } else {
                // 체크인 날짜가 아직 선택되지 않은 경우 체크인 날짜로 설정
                if (!isCheckInSelected) {
                    checkInDate = startDate;
                    checkOutDate = new Date(new Date(checkInDate).setDate(new Date(checkInDate).getDate() + 1)).toISOString().slice(0, 10); // 체크아웃을 1박 2일 후로 설정
                    isCheckInSelected = true; // 체크인 날짜가 설정되었음을 표시
                } else {
                    // 체크아웃 날짜는 체크인 이후 날짜만 선택 가능
                    if (new Date(startDate) > new Date(checkInDate)) {
                        checkOutDate = startDate;
                        isCheckOutSelected = true; // 체크아웃이 선택되었음을 표시
                    }
                }
            }

            // 날짜 업데이트
            document.getElementById('checkInDate').value = checkInDate;
            document.getElementById('checkOutDate').value = checkOutDate;

            // 체크인 날짜 업데이트
            document.getElementById('checkInDisplay').innerText = formatDate(new Date(checkInDate));

            // 체크아웃 날짜 업데이트
            document.getElementById('checkOutDisplay').innerText = formatDate(new Date(checkOutDate));

            // 선택된 날짜 스타일링을 초기화하고 새롭게 반영
            calendar.getEvents().forEach(event => event.remove());

            if (checkInDate) {
                // 체크인 날짜 표시
                calendar.addEvent({
                    start: checkInDate,
                    end: new Date(new Date(checkOutDate).setDate(new Date(checkOutDate).getDate() + 1)).toISOString().slice(0, 10),
                    display: 'background',
                    classNames: ['selected']
                });
            }

            if (checkInDate && checkOutDate) {
                // 체크인과 체크아웃 사이의 날짜 범위 표시
                calendar.addEvent({
                    start: new Date(new Date(checkInDate).setDate(new Date(checkInDate).getDate() + 1)).toISOString().slice(0, 10),
                    end: new Date(checkOutDate).toISOString().slice(0, 10),
                    display: 'background',
                    classNames: ['selected']
                });
            }
        },
        unselectAuto: false
    });

    // 초기 상태에서 체크인 및 체크아웃 날짜 반영
    calendar.addEvent({
        start: checkInDate,
        end: new Date(new Date(checkOutDate).setDate(new Date(checkOutDate).getDate() + 1)).toISOString().slice(0, 10),
        display: 'background',
        classNames: ['selected']
    });

    if (checkInDate && checkOutDate) {
        calendar.addEvent({
            start: new Date(new Date(checkInDate).setDate(new Date(checkInDate).getDate() + 1)).toISOString().slice(0, 10),
            end: new Date(checkOutDate).toISOString().slice(0, 10),
            display: 'background',
            classNames: ['selected']
        });
    }

    calendar.render();
});

toggleButton.addEventListener('click', function () {
    if (calendarDiv.style.display === 'none') {
        calendarDiv.style.display = 'block';
        calendar.render(); // FullCalendar를 다시 렌더링
        toggleButton.textContent = '숨기기'; // 버튼 텍스트 변경
    } else {
        calendarDiv.style.display = 'none';
        toggleButton.textContent = '보이기'; // 버튼 텍스트 변경
    }
});

function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`; // "YYYY-MM-DD" 형식
}

function validateForm() {
    return true; // 폼을 제출할 수 있도록 true 반환
}
function setSelectedCampsite(campsiteId) {
    // Set the campsite ID in the hidden input field
    console.log("select campsite", campsiteId);
    document.getElementById('campsiteId').value = campsiteId;
    // Automatically submit the form
    document.querySelector('form').submit();
}