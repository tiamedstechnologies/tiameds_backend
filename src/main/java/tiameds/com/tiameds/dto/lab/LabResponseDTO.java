package tiameds.com.tiameds.dto.lab;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabResponseDTO {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String description;
    private UserResponseDTO createdBy;
}