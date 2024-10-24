$(document).ready(function() {
    // 페이지 로드 시 location 목록을 가져오는 AJAX 요청
    $.ajax({
        url: '/api/locations',  // 서버에서 location 목록을 반환하는 URL
        type: 'GET',
        success: function(data) {
            console.log(data);

            // 가져온 location 목록으로 locationSelect를 업데이트
            $('#locationSelect').empty().append('<option value="">지역</option>');
            $.each(data, function(index, location) {
                $('#locationSelect').append(
                    $('<option></option>').val(location.id).text(location.sido)
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('AJAX 요청 실패:', error);
        }
    });

    $('#locationSelect').change(function() {
        const selectedLocationId = $(this).val();

        // 선택된 지역 ID가 비어있지 않은 경우 AJAX 요청
        if (selectedLocationId) {
            $.ajax({
                url: '/api/locations/' + selectedLocationId + '/details',  // 서버에서 세부 지역을 가져오는 URL
                type: 'GET',
                data: { locationId: selectedLocationId },  // 선택한 지역 ID 전송
                success: function(data) {
                    // 세부 지역 셀렉트 초기화
                    $('#locationDetailSelect').empty().append('<option value="">세부 지역 선택</option>');

                    // 받은 세부 지역 추가
                    $.each(data, function(index, locationDetail) {
                        $('#locationDetailSelect').append(
                            $('<option></option>').val(locationDetail.id).text(locationDetail.sigungu)
                        );
                    });
                },
                error: function(xhr, status, error) {
                    console.error('AJAX 요청 실패:', error);
                }
            });
        } else {
            // 지역 선택이 해제된 경우 세부 지역 초기화
            $('#locationDetailSelect').empty().append('<option value="">세부 지역 선택</option>');
        }
    });

    $('#locationDetailSelect').change(function() {
        const selectedLocationDetailId = $(this).val();
        $('#locationDetailId').val(selectedLocationDetailId);  // 숨겨진 필드에 값 설정
    });

    // 페이지 로드 시 선택된 지역에 대한 세부 지역 로드
    const initialLocationId = $('#locationSelect').val();
    if (initialLocationId) {
        $('#locationSelect').trigger('change'); // 지역 선택 이벤트 강제로 발생
    }
});