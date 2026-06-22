package backend.nemra.modules.auth.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class RegisterClient extends RegisterRequest {

}
