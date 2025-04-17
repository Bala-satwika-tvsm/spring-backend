package com.tvse.callrecordingsbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallRecordingsEntity {
    private String phone_number;
    private String Call_Id;
    private String Call_Duration;
    private String Status;
    private String Call_Date;

    private String ProjectName;
    private String UnitNumber;
    private String ApartmentName;
    private String Phase;
    private String UnitStatus;

}
