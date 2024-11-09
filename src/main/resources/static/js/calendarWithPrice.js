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
    checkInDate = urlParams.get('checkInDate') || new Date().toISOString().slice(0, 10); // 오늘 날짜
    checkOutDate = urlParams.get('checkOutDate') || new Date(new Date().setDate(new Date().getDate() + 1)).toISOString().slice(0, 10); // 내일 날짜

    const today = new Date();
    const maxDate = new Date();
    maxDate.setDate(today.getDate() + 92); // 오늘 날짜로부터 92일 후

    // 날짜를 input 요소에 설정
    document.getElementById('checkInDate').value = checkInDate;
    document.getElementById('checkOutDate').value = checkOutDate;

    // 화면에 표시될 날짜 설정
    document.getElementById('checkInDisplay').innerText = checkInDate;
    document.getElementById('checkOutDisplay').innerText = checkOutDate;

    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        selectable: true,
        select: function (info) {
            var startDateStr = info.startStr;
            var startDate = new Date(info.startStr);
            if (startDate < today || startDate > maxDate) {
                return;
            }

            // 체크인 날짜가 선택된 상태에서 이전 날짜가 선택된 경우
            if (isCheckInSelected && startDate < new Date(checkInDate)) {
                checkOutDate = checkInDate; // 기존 체크인 날짜를 체크아웃 날짜로 설정
                checkInDate = startDateStr; // 새로 선택된 날짜를 체크인 날짜로 설정
                isCheckOutSelected = true; // 체크아웃 날짜가 설정되었음을 표시
            } else {
                // 체크아웃이 선택된 상태일 때 새로운 체크인 날짜를 선택하도록 설정
                if (isCheckOutSelected) {
                    checkInDate = startDateStr;
                    checkOutDate = new Date(new Date(checkInDate).setDate(new Date(checkInDate).getDate() + 1)).toISOString().slice(0, 10);
                    isCheckOutSelected = false;
                } else {
                    // 체크인 날짜가 아직 선택되지 않은 경우 체크인 날짜로 설정
                    if (!isCheckInSelected) {
                        checkInDate = startDateStr;
                        checkOutDate = new Date(new Date(checkInDate).setDate(new Date(checkInDate).getDate() + 1)).toISOString().slice(0, 10); // 체크아웃을 1박 2일 후로 설정
                        isCheckInSelected = true; // 체크인 날짜가 설정되었음을 표시
                    } else {
                        // 체크아웃 날짜는 체크인 이후 날짜만 선택 가능
                        if (new Date(startDate) > new Date(checkInDate)) {
                            checkOutDate = startDateStr;
                            isCheckOutSelected = true; // 체크아웃이 선택되었음을 표시
                        }
                    }
                }
            }

            // AJAX 요청 및 총 가격 업데이트
            $.ajax({
                url: '/api/campsite/list',
                type: 'GET',
                data: {
                    campgroundId: document.getElementById('campgroundId').value,
                    checkInDate,
                    checkOutDate
                },
                success: function (data) {
                    $.each(data, function (campsiteId, totalPrice) {
                        // 해당 캠프사이트의 행을 찾기
                        var campsiteRow = document.getElementById('campsite-row-' + campsiteId);
                        if (campsiteRow) {
                            // 총 가격을 업데이트
                            var totalPriceCell = campsiteRow.querySelector('.total-price');
                            if (totalPriceCell) {
                                totalPriceCell.textContent = '총가격 : ' + totalPrice + ' 원'; // 총 가격 업데이트
                            }
                        }
                    });

                    // 예약된 캠프사이트 데이터를 받아와 상태를 설정
                    $.ajax({
                        url: '/api/campsite/booked',
                        type: 'GET',
                        data: {
                            campgroundId: document.getElementById('campgroundId').value,
                            checkInDate,
                            checkOutDate
                        },
                        success: function (bookedCampsites) {
                            // 예약된 캠프사이트를 기반으로 버튼 및 상태 표시 업데이트
                            var allCampsiteRows = document.querySelectorAll('tr[id^="campsite-row-"]');
                            allCampsiteRows.forEach(function (campsiteRow) {
                                var campsiteId = campsiteRow.id.split('-')[2]; // 'campsite-row-<id>'에서 id 추출
                                var bookingBtn = campsiteRow.querySelector('.booking-btn');
                                var bookingStatus = campsiteRow.querySelector('.booking-status');

                                // 현재 캠프사이트가 예약된 상태인지 확인
                                if (bookedCampsites.some(campsite => campsite.id == campsiteId)) {
                                    // 예약된 경우
                                    if (bookingBtn) bookingBtn.style.display = 'none'; // 예약 버튼 숨기기
                                    if (bookingStatus) bookingStatus.style.display = 'inline'; // 예약 불가 표시 보이기
                                } else {
                                    // 예약 가능 상태
                                    if (bookingBtn) bookingBtn.style.display = 'inline-block'; // 예약 버튼 보이기
                                    if (bookingStatus) bookingStatus.style.display = 'none'; // 예약 불가 숨기기
                                }
                            });
                        },
                        error: function (xhr, status, error) {
                            console.error('예약된 캠프사이트 요청 오류:', status, error);
                        }
                    });
                },
                error: function (xhr, status, error) {
                    console.error('AJAX 요청 오류:', status, error);
                }
            });

            // 날짜 업데이트
            document.getElementById('checkInDate').value = checkInDate;
            document.getElementById('checkOutDate').value = checkOutDate;

            document.getElementById('checkInDisplay').innerText = checkInDate;
            document.getElementById('checkOutDisplay').innerText = checkOutDate;

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

function validateForm() {
    return true; // 폼을 제출할 수 있도록 true 반환
}

function setSelectedCampsite(campsiteId) {
    // Set the campsite ID in the hidden input field
    document.getElementById('campsiteId').value = campsiteId;
    // Automatically submit the form
    document.getElementById('bookingForm').submit();
}
