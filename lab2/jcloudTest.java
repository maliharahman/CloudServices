package lab2;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
//import org.jclouds.aws.
import com.amazonaws.util.IOUtils;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import org.apache.commons.codec.Charsets;
import org.jclouds.ContextBuilder;
import org.jclouds.aws.domain.Region;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.ContainerAccess;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationScope;
import org.jclouds.location.predicates.LocationPredicates;
import org.jclouds.sshj.config.SshjSshClientModule;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class jcloudTest {

    public static void createContainer(BlobStoreContext context,BlobStore blobStore, String bucketName)
    {
        //create a container in default location

        //set user define location
        Location location = Iterables.tryFind(blobStore.listAssignableLocations(),
                LocationPredicates.idEquals(Region.US_WEST_1)).orNull();

        boolean created=blobStore.createContainerInLocation(null,bucketName);

        //check container access: public or private
        System.out.println(blobStore.getContainerAccess(bucketName).toString());

        //list assignable location
        for(int i=0;i<blobStore.listAssignableLocations().size();++i)
            System.out.println(blobStore.listAssignableLocations().toArray()[i]);

        //check if container already exists
        if(created)
            System.out.println("New container created: " + bucketName);
        else
            System.out.println("Container already exists!");
    }


    public static void downloadBlob(Blob blob) throws IOException
    {

        InputStream is = blob.getPayload().getInput();
        File downloadFile = new File("/home/maliha/blob.pdf");
        OutputStream outputStream = new FileOutputStream(downloadFile);

        byte[] buffer = new byte[8*1024];
        int bytesRead;

        while((bytesRead = is.read(buffer)) != -1)
        {
            outputStream.write(buffer, 0, bytesRead);
        }

        is.close();
        outputStream.close();
        System.out.println("File Downloaded Successfully!");
    }


    public static void uploadBlob(BlobStore blobStore, String bucketName) throws IOException
    {
        ByteSource payload = Files.asByteSource(new File("/home/maliha/maliha.pdf"));
        Blob uploadBlob = blobStore.blobBuilder("maliha.pdf")
                .payload(payload)
                .contentLength(payload.size())
                .build();

        // Upload the Blob
        blobStore.putBlob(bucketName, uploadBlob);
        System.out.println("File uploaded successfully!");
    }


    public static void listContainers(BlobStore blobStore) throws IOException
    {
        for (int i = 0; i < blobStore.list().size(); i++)
        {
            System.out.println(blobStore.list().toArray()[i]);
        }
    }

    public static void deleteBlob(BlobStore blobStore, String bucketName, String blobName)
    {
        blobStore.removeBlob(bucketName,blobName);
        System.out.println("File Deleted Successfully!");
    }


    public static void main(String[] args) throws IOException {

        //String bucketName="";
        String bucketName="";
        String blobName="";

        //get a amazon context
        BlobStoreContext context = ContextBuilder.newBuilder("aws-s3")
                .credentials("access key","secret access key")
                .buildView(BlobStoreContext.class);

        BlobStore blobStore = context.getBlobStore();
        Blob downloadBlob = blobStore.getBlob(bucketName,"");

        downloadBlob(downloadBlob);
        //deleteBlob(blobStore, bucketName, blobName);
        //uploadBlob(blobStore, bucketName);
        //listContainers(blobStore);
        //createContainer(context,blobStore,bucketName);
        //add blob
       // Blob blob = blobStore.newBlob("");
    }
}
