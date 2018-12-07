package lab1;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class experimentBucket {

    private static int flag=0;
    public GUI callgui;


    public static void createBucketNewRegion(String bucket_name, String region)
    {
        final AmazonS3 storage = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new ProfileCredentialsProvider())
                .build();



        Bucket b=null;
        List<Bucket> buckList = storage.listBuckets();
        for(Bucket buckExist : buckList)
        {
            if(buckExist.getName().equals(bucket_name))
            {
                System.out.println("Bucket already exists!");
                b=buckExist;
                flag=-1;
            }
        }

        if(flag!=-1)
        {
            b=storage.createBucket(bucket_name);
            System.out.println("Bucket created!! Name: " + b.getName() );
        }

        return;
    }


    public void createBucket(String bucket_name)
        {
            final AmazonS3 storage = AmazonS3ClientBuilder.defaultClient();


            Bucket b=null;
            List<Bucket> buckList = storage.listBuckets();
            for(Bucket buckExist : buckList)
            {
                if(buckExist.getName().equals(bucket_name))
                {
                    System.out.println("Bucket already exists!");

                    b=buckExist;
                    flag=-1;
                }
            }

            if(flag!=-1)
            {
                b=storage.createBucket(bucket_name);
                System.out.println("Bucket created!! Name: " + b.getName() );
            }

            //callgui.setTitle(bucket_name);
            //callgui = new GUI(bucket_name);
            return;
        }



    public String checkBucketRegion(String region)
    {

        String finalString = " ";

        String buck1="ca-central-1", buck2="us-west-2", buck3="eu-central-1";
        //AmazonS3 s3;

        AmazonS3 client = null;

        if(region==buck1)
        {
            client= AmazonS3Client.builder().withRegion(buck1).withPathStyleAccessEnabled(true).build();
            //s3 = AmazonS3ClientBuilder.standard().withRegion(buck1).build();
            System.out.println("inside: " + buck1);
        }
        else if(region==buck2) {
            client= AmazonS3Client.builder().withRegion(buck2).withPathStyleAccessEnabled(true).build();
           //s3 = AmazonS3ClientBuilder.standard().withRegion(buck2).build();
            System.out.println("inside: " + buck2);
        }
        else if(region==buck3)
        {
            client= AmazonS3Client.builder().withRegion(buck3).withPathStyleAccessEnabled(true).build();
            //s3 = AmazonS3ClientBuilder.standard().withRegion(buck3).build();
            System.out.println("inside: " + buck3);
        }

        List<Bucket> buckList = client.listBuckets();
        System.out.println("Region: " + region + " Buckets are: ");

        for(Bucket buckExist : buckList)
        {
            try {
                //String loc = s3.headBucket(new HeadBucketRequest(buckExist.getName())).getBucketRegion();
                if(client.getBucketLocation(buckExist.getName()).equals(region))
                {
                    finalString  = finalString +  buckExist.getName() + "\n";
                }
            }catch(Exception e)
            {
                System.out.println(e.toString());
            }
        }
        return finalString;
    }


    public void deleteBucket(String bucket_name, String region) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();

        try {
            System.out.println(" - removing objects from bucket");
            ObjectListing object_listing = s3.listObjects(bucket_name);
            while (true) {
                for (Iterator<?> iterator =
                     object_listing.getObjectSummaries().iterator();
                     iterator.hasNext();) {
                    S3ObjectSummary summary = (S3ObjectSummary)iterator.next();
                    s3.deleteObject(bucket_name, summary.getKey());
                }

                // more object_listing to retrieve?
                if (object_listing.isTruncated()) {
                    object_listing = s3.listNextBatchOfObjects(object_listing);
                } else {
                    break;
                }
            };
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

        s3.deleteBucket(bucket_name);

        return;
    }



    public void deleteObject(String bucket_name, String object_name, String region) throws InterruptedException {
        final AmazonS3 s3=AmazonS3ClientBuilder.standard().withRegion(region).build();

        System.out.println("Before deleting objects >> Traverse the list: ");
        ListObjectsV2Result result = s3.listObjectsV2(bucket_name);
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        for(S3ObjectSummary os: objects)
            System.out.println("Objects: " + os.getKey());
    try
    {
        s3.deleteObject(bucket_name,object_name);
        System.out.println("Deleted bukcet object!");
    }
    catch(AmazonServiceException ex)
    {
        System.err.println(ex.getErrorMessage());
        System.exit(1);
    }

        System.out.println("After deleting objects>> Traverse the list: ");
    Thread th=Thread.currentThread();
    th.sleep(3000);

        for(S3ObjectSummary os: objects)
            System.out.println("Objects: " + os.getKey());
    }


    public void downloadObject(String bucket_name, String object_name, String filePath,  String region)
    {
        System.out.println("Downloading buckets....");
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
        try
        {
            S3Object o = s3.getObject(bucket_name,object_name);
            S3ObjectInputStream obj = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            byte[] read_buf = new byte[1024];
            int read_len=0;
            while((read_len=obj.read(read_buf))>0)
            {
                fos.write(read_buf,0,read_len);
            }

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void uploadObject(String bucket_name, String object_name, String filePath, String region)
    {
     AmazonS3 client=AmazonS3ClientBuilder.standard().withRegion(region).build();
     PutObjectRequest request = new PutObjectRequest
             (bucket_name,object_name, new File(filePath));
     ObjectMetadata metadata = new ObjectMetadata();
     metadata.setContentType("plain/text");
     metadata.addUserMetadata("my new pdf", "Title");
     request.setMetadata(metadata);
     client.putObject(request);
    }


    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.out.println("Please enter a name: ");
            System.exit(1);
        }

        //String bucket_name = args[0];

        String bucket_name = "malihajose";
        String object_name="snow.png";
        String region="us-west-2";
        String filePath="/home/maliha/Desktop/";

        System.out.println("\nCreating S3 bucket: " + bucket_name);

        //deleteBucket(bucket_name,region);
       // String regionsList=checkBucketRegion(region);
      //  System.out.println("bucket in " + region + " : " + regionsList);
        //createBucket(bucket_name);
        createBucketNewRegion(bucket_name, region);
        //deleteObject(bucket_name,object_name,region);
       // downloadObject(bucket_name,object_name, region);
       // uploadObject(bucket_name,object_name,filePath,region);
    }
}