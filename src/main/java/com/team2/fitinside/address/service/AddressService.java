package com.team2.fitinside.address.service;

import com.team2.fitinside.address.dto.AddressRequestDto;
import com.team2.fitinside.address.dto.AddressResponseDto;
import com.team2.fitinside.address.entity.Address;
import com.team2.fitinside.address.mapper.AddressMapper;
import com.team2.fitinside.address.repository.AddressRepository;
import com.team2.fitinside.config.SecurityUtil;
import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.member.entity.Member;
import com.team2.fitinside.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.team2.fitinside.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AddressService {

    private static final int MAX_ADDRESS_LIMIT = 5;

    private final SecurityUtil securityUtil;
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    public List<AddressResponseDto> findAllAddresses() {

        Long loginMemberId = securityUtil.getCurrentMemberId();
        List<Address> addresses = addressRepository.findAllByMemberId(loginMemberId);

        return addressMapper.toAddressResponseDtoList(addresses);
    }

    public AddressResponseDto findAddress(Long addressId) {

        Address address = addressRepository.findByIdAndIsDeletedFalse(addressId)
                .orElseThrow(() -> new CustomException(ADDRESS_NOT_FOUND));

        checkAuthorization(address);
        return addressMapper.toAddressResponseDto(address);
    }

    public AddressResponseDto findDefaultAddress() {
        // 회원의 전체 목록에서 isDefault = ture인 배송지 찾기
        Long loginMemberId = securityUtil.getCurrentMemberId();
        Optional<Address> address = addressRepository.findByMemberIdAndDefaultAddress(loginMemberId, "Y");

        if (address.isEmpty()) return null;
        return addressMapper.toAddressResponseDto(address.get());
    }

    @Transactional
    public AddressResponseDto createAddress(AddressRequestDto request) {

        Long loginMemberId = securityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 기본 배송지 설정
        if (request.getDefaultAddress().equals("Y")) {
            changeToNotDefaultAddress(); // 기존의 기본 배송지를 N으로 변경

            // 중복된 배송지가 있으면 기본 배송지로 설정만 변경하고 반환
            Address existingAddress = findDuplicateAddress(addressRepository.findAllByMemberId(loginMemberId), request);
            if (existingAddress != null) {
                existingAddress.checkDefault("Y");
                return addressMapper.toAddressResponseDto(existingAddress);
            }
        }

        // 중복된 배송지가 있으면 반환, 없으면 저장
        return saveNewOrReturnExistingAddress(findMember, request);
    }

    @Transactional
    public AddressResponseDto updateAddress(Long addressId, AddressRequestDto request) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException(ADDRESS_NOT_FOUND));

        checkAuthorization(address);

        if(request.getDefaultAddress().equals("Y")){
            changeToNotDefaultAddress();
        }

        address.updateAddress(request);
        return addressMapper.toAddressResponseDto(address);
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException(ADDRESS_NOT_FOUND));

        checkAuthorization(address);
        address.deleteAddress();
    }

    // 기존에 저장한 배송지의 기본 배송지 상태 변경
    @Transactional
    public void checkDefault(Long addressId, String isDefault) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException(ADDRESS_NOT_FOUND));

        checkAuthorization(address);

        if (isDefault.equals("Y")) {
            changeToNotDefaultAddress();
        }

        address.checkDefault(isDefault);
    }

    private void checkAuthorization(Address address) {
        Long loginMemberId = securityUtil.getCurrentMemberId();
        if (!loginMemberId.equals(address.getMember().getId())) {
            throw new CustomException(USER_NOT_AUTHORIZED);
        }
    }

    // 중복 배송지 검사: 수령인, 전화번호, 우편번호, 상세주소 중 하나라도 다르면 추가
    private boolean isDuplicate(Address exist, AddressRequestDto request) {
        return exist.getDeliveryReceiver().equals(request.getDeliveryReceiver()) &&
                exist.getDeliveryPhone().equals(request.getDeliveryPhone()) &&
                exist.getPostalCode().equals(request.getPostalCode()) &&
                exist.getDetailedAddress().equals(request.getDetailedAddress());
    }

    // 배송지 최대 개수 검사
    private void validateAddressLimit(Long loginMemberId) {
        if (addressRepository.findAllByMemberId(loginMemberId).size() >= MAX_ADDRESS_LIMIT) {
            throw new CustomException(EXCEEDED_MAX_ADDRESS_LIMIT);
        }
    }

    // 기본 배송지 설정 해제
    private void changeToNotDefaultAddress() {
        Long loginMemberId = securityUtil.getCurrentMemberId();
        Optional<Address> defaultAddress = addressRepository.findByMemberIdAndDefaultAddress(loginMemberId, "Y");
        defaultAddress.ifPresent(address -> address.checkDefault("N"));
    }

    // 중복된 주소 찾기
    private Address findDuplicateAddress(List<Address> addresses, AddressRequestDto request) {
        return addresses.stream()
                .filter(existingAddress -> isDuplicate(existingAddress, request))
                .findFirst()
                .orElse(null);
    }

    // 중복된 주소가 있으면 반환, 없으면 저장
    private AddressResponseDto saveNewOrReturnExistingAddress(Member findMember, AddressRequestDto request) {
        // 중복된 주소가 있는지 확인
        Address duplicateAddress = findDuplicateAddress(addressRepository.findAllByMemberId(findMember.getId()), request);
        if (duplicateAddress != null) {
            return addressMapper.toAddressResponseDto(duplicateAddress);
        }

        // 중복된 주소가 없으면 새 주소 저장
        validateAddressLimit(findMember.getId()); // 이미 존재하는 배송지가 5개면 저장X
        Address newAddress = addressMapper.toAddress(request);
        newAddress.setMember(findMember);
        Address savedAddress = addressRepository.save(newAddress);

        return addressMapper.toAddressResponseDto(savedAddress);
    }


}
