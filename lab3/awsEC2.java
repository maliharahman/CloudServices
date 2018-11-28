package lab3;

import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.elasticache.model.CreateCacheSecurityGroupRequest;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.DescribeKeyPairsResult;
import com.amazonaws.services.ec2.model.KeyPairInfo;

import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.s3.model.Bucket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.ListMetricsRequest;
import com.amazonaws.services.cloudwatch.model.ListMetricsResult;
import com.amazonaws.services.cloudwatch.model.Metric;
import org.jclouds.packet.domain.IpAddress;

import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;

import software.amazon.awssdk.regions.internal.util.EC2MetadataUtils;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.RunInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Tag;
import software.amazon.awssdk.services.ec2.model.CreateTagsRequest;

import software.amazon.awssdk.services.ec2.model.InstanceType;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.RunInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Tag;
import software.amazon.awssdk.services.ec2.model.CreateTagsRequest;
import software.amazon.awssdk.services.ec2.model.StopInstancesRequest;


public class awsEC2 {

    //create, describe and delete specific security group
    public static void createSecurityGroup(String groupName,String groupDesc,String vpc_id,String region,AmazonEC2 ec2)
    {
        //final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        //check if the security group already exists or not
        String groupID;
            CreateSecurityGroupRequest create_request = new CreateSecurityGroupRequest()
                    .withGroupName(groupName)
                    .withDescription(groupDesc)
                    .withVpcId(vpc_id);

            CreateSecurityGroupResult create_response = ec2.createSecurityGroup(create_request);
            System.out.println(create_response.toString());
            System.out.println("Succeessfully created security group name: " + groupName);

            groupID=create_response.getGroupId();
            System.out.println("Security group ID is: " + groupID);
    }


    //describe security group with group-name
    public static void describeSecurityGroup(String groupName, AmazonEC2 ec2)
    {
        DescribeSecurityGroupsRequest secGroupReq=new DescribeSecurityGroupsRequest()
                .withGroupNames(groupName);
        DescribeSecurityGroupsResult secGrpRes=ec2.describeSecurityGroups(secGroupReq);

        for(SecurityGroup group : secGrpRes.getSecurityGroups())
        {
            System.out.printf(
                    "Found security group with id %s, " +
                            "vpc id %s " +
                            "and description %s",
                    group.getGroupId(),
                    group.getVpcId(),
                    group.getDescription());
        }
    }



    //delete security group with group name
    public static void deleteSecurityGroup(String groupName, String region, AmazonEC2 ec2)
    {
        DeleteSecurityGroupRequest request=new DeleteSecurityGroupRequest().withGroupName(groupName);
        //DeleteSecurityGroupRequest request = new DeleteSecurityGroupRequest()
          //      .withGroupId(groupID);

        DeleteSecurityGroupResult response = ec2.deleteSecurityGroup(request);
        System.out.println("Successfully deleted security group name: " + groupName);
    }


    //with 0.0.0.0/0 or any IP range
    public static void authorizeSecurityGroup(String groupName, AmazonEC2 ec2)
    {
        //line 98-111 : setting user defined IP ranges to security groups
        /*IpPermission ipPermission = new IpPermission();
        IpRange ipRange1 = new IpRange().withCidrIp("0.0.0.0/32");
        IpRange ipRange2 = new IpRange().withCidrIp("100.10.10.10/32");
        ipPermission.withIpv4Ranges(Arrays.asList(new IpRange[] {ipRange1, ipRange2}))
                .withIpProtocol("tcp")
                .withFromPort(22)
                .withToPort(22);

        AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest =
                new AuthorizeSecurityGroupIngressRequest();
        authorizeSecurityGroupIngressRequest.withGroupName(groupName)
                .withIpPermissions(ipPermission);

        ec2.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);*/

        //assigning access to anywhere
        IpRange ip_range = new IpRange()
                .withCidrIp("0.0.0.0/0");

        IpPermission ip_perm = new IpPermission()
                .withIpProtocol("tcp")
                .withToPort(80)
                .withFromPort(80)
                .withIpv4Ranges(ip_range);

        IpPermission ip_perm2 = new IpPermission()
                .withIpProtocol("tcp")
                .withToPort(22)
                .withFromPort(22)
                .withIpv4Ranges(ip_range);

        AuthorizeSecurityGroupIngressRequest auth_request = new
                AuthorizeSecurityGroupIngressRequest()
                .withGroupName(groupName)
                .withIpPermissions(ip_perm, ip_perm2);

        AuthorizeSecurityGroupIngressResult auth_response =
                ec2.authorizeSecurityGroupIngress(auth_request);
    }


    //list EC2 regions and end-points
    public static void listRegion(AmazonEC2 ec2)
    {
        DescribeRegionsResult regions_response = ec2.describeRegions();

        for(Region region : regions_response.getRegions()) {
            System.out.println("Found region" + region.getRegionName()
                    + "with endpoint" + region.getEndpoint());
        }
    }


    public static void createInstance(String amiID, String keyName, String groupName, String region, Ec2Client ec3)
    {
        //create instance using AmazonEC2 method
        /*
        AmazonEC2 ec2Client = AmazonEC2ClientBuilder
                .standard()
                .withRegion(region)
                .build();

        RunInstancesRequest runInstancesRequest = new RunInstancesRequest()
                .withImageId(amiID)
                .withInstanceType("t2.micro")
                .withKeyName(keyName)
                .withMinCount(1)
                .withMaxCount(1)
                .withSecurityGroups(groupName);
        System.out.println(runInstancesRequest);
        String yourInstanceId = ec2Client.runInstances(runInstancesRequest)
                .getReservation().getInstances().get(0).getInstanceId();
        System.out.println(yourInstanceId);

        Tag tag = Tag.builder()
                .key("maliJose")
                .value(keyName)
                .build();

        CreateTagsRequest tagReq= CreateTagsRequest.builder().tags(tag).build();*/


        //create instance using EC2client method
        software.amazon.awssdk.services.ec2.model.RunInstancesRequest run_request =
                software.amazon.awssdk.services.ec2.model.RunInstancesRequest.builder()
                        .securityGroups(groupName)
                        .keyName(keyName)
                .imageId(amiID)
                .instanceType(software.amazon.awssdk.services.ec2.model.InstanceType.T2_MICRO)
                .maxCount(1)
                .minCount(1)
                .build();

        software.amazon.awssdk.services.ec2.model.RunInstancesResponse response = ec3.runInstances(run_request);

        String instance_id = response.reservationId();

        Tag tag = Tag.builder()
                .key("new instance")
                .value(keyName)
                .build();

        software.amazon.awssdk.services.ec2.model.CreateTagsRequest tag_request = CreateTagsRequest.builder()
                .tags(tag)
                .build();

        try {
            ec3.createTags(tag_request);

            System.out.printf(
                    "Successfully started EC2 instance %s based on AMI %s",
                    instance_id, amiID);
        }
        catch (Ec2Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


    public static void stopInstance(String instanceID)
    {
        Ec2Client ec2 = Ec2Client.create();
        StopInstancesRequest request = StopInstancesRequest.builder()
                .instanceIds(instanceID).build();
        ec2.stopInstances(request);
    }


    //describe attributes of an instance
    public static void describeInstance(AmazonEC2 ec2)
    {
        boolean done = false;

        DescribeInstancesRequest request = new DescribeInstancesRequest();
        while(!done) {
            DescribeInstancesResult response = ec2.describeInstances(request);

            for(Reservation reservation : response.getReservations()) {
                for(Instance instance : reservation.getInstances()) {
                    System.out.println("Instance with id " +  instance.getInstanceId()
                                    + " AMI " + instance.getKeyName()
                                    + " type " + instance.getInstanceType()
                                    + " state " +   instance.getState().getName()
                                    + " and monitoring state" + instance.getMonitoring().getState() + "\n");
                }
            }

            request.setNextToken(response.getNextToken());

            if(response.getNextToken() == null) {
                done = true;
            }
        }
    }


    public static void createKeyPair(String keyName,AmazonEC2 ec2)
    {
        CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest();
        createKeyPairRequest.withKeyName(keyName);
        CreateKeyPairResult createKeyPairResult =
                ec2.createKeyPair(createKeyPairRequest);
        KeyPair keyPair = new KeyPair();

        keyPair = createKeyPairResult.getKeyPair();

    }


    public static void listKeyPair(AmazonEC2 ec2)
    {
        DescribeKeyPairsResult response = ec2.describeKeyPairs();

        for(KeyPairInfo keyPair : response.getKeyPairs()) {
            System.out.println("Found key pair with name" + keyPair.getKeyName()
                           + "and fingerprint" + keyPair.getKeyFingerprint());
        }
    }


    public static void main(String[] args)
    {
        String groupName="";
        String groupDesc="";
        String vpc_id="";
        String region="eu-west-1"; //ireland
        String keyName="";
        String amiID = "";
        String instanceID="";


        final AmazonEC2 ec2=AmazonEC2ClientBuilder.standard().withRegion(region).build();
        Ec2Client ec3 = software.amazon.awssdk.services.ec2.Ec2Client.builder().region(software.amazon.awssdk.regions.Region.of(region)).build();

        describeInstance(ec2);
        //stopInstance(instanceID);
        //listKeyPair(ec2);
        //createKeyPair(keyName,ec2);
        //createInstance(ami_id, keyName, groupName, region,ec3);
        //authorizeSecurityGroup(groupName,ec2);
        //describeSecurityGroup(groupName,ec2);
        //listRegion(ec2);
        //createSecurityGroup(groupName,groupDesc,vpc_id,region,ec2);
        //deleteSecurityGroup(groupName,region,ec2);
    }
}
