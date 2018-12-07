package lab1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI extends JFrame
{
    experimentBucket callObj = new experimentBucket();

    JPanel mainPanel=new JPanel();

    JLabel labelCreate= new JLabel("Bucket Name: ");
    JTextField bucket=new JTextField(70);
    JLabel labelRegion = new JLabel("Select region for bucket list: ");
    JLabel bucketName = new JLabel();
    JButton buttonCreate = new JButton("Create");

    JLabel labelDelete= new JLabel("Bucket Name: ");
    JTextField bucketDelete=new JTextField(70);
    JLabel deleteBuck = new JLabel();
    JButton buttonDelete = new JButton("Delete");

    JLabel labelObj= new JLabel("Object Name: ");
    JTextField bucketObj=new JTextField(70);
    JLabel labelDelBuck = new JLabel("Bucket Name: ");
    JTextField deleteBuckField = new JTextField(70);
    JButton buttonObj = new JButton("Delete Object");

    JLabel labelDeleteBucket = new JLabel("Delete bucket: ");
    JTextField fieldDeleteBucket = new JTextField(70);
    JButton buttonDeleteBucket = new JButton("Delete bucket");

    JLabel labelObjectName = new JLabel("Object name:");
    JTextField fieldUploadObject = new JTextField(70);
    JLabel labelUploadBuck = new JLabel("Bucket name: ");
    JTextField fieldUploadBuck = new JTextField(70);
    JLabel labelFilePath = new JLabel("File path: ");
    JTextField fieldUploadFilePath = new JTextField(70);
    JButton buttonUpload = new JButton("Upload files");

    String[] choices = { "eu-central-1", "us-west-2", "ca-central-1"};

    final JComboBox<String> cb = new JComboBox<String>(choices);

    String[] choices1= { "eu-central-1", "us-west-2", "ca-central-1"};

    final JComboBox<String> cb1 = new JComboBox<String>(choices1);

    String[] choices2= { "eu-central-1", "us-west-2", "ca-central-1"};

    final JComboBox<String> cb2 = new JComboBox<String>(choices2);

    String[] choices3= { "eu-central-1", "us-west-2", "ca-central-1"};

    final JComboBox<String> cb3 = new JComboBox<String>(choices3);

    String[] choices5= { "eu-central-1", "us-west-2", "ca-central-1"};

    final JComboBox<String> cb5 = new JComboBox<String>(choices5);


    //check bucket region
    JLabel labelBucketRegion = new JLabel("Check buckets from region: ");
    JTextArea areaBucket=new JTextArea();
    String[] choices4= { "eu-central-1", "us-west-2", "ca-central-1"};
    final JComboBox<String> cb4 = new JComboBox<String>(choices4);
    JButton buttonCheckRegion=new JButton("Submit");

    JButton buttonDownload = new JButton("Download");


    public GUI()
    {
        setTitle("AWS S3");
        setVisible(true);
        setSize(900,1200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainPanel.setBackground(Color.PINK);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));

        //create bucket
        labelCreate.setPreferredSize(new Dimension(700,50));
        labelCreate.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPanel.add(labelCreate);
        mainPanel.add(bucket);
        mainPanel.add(labelRegion);
        cb1.setPreferredSize(new Dimension(200,50));
        mainPanel.add(cb1);
        buttonCreate.setPreferredSize(new Dimension(200,50));
        buttonCreate.setBackground(Color.orange);
        mainPanel.add(buttonCreate);
        add(mainPanel);

        //delete bucket
        labelDelete.setPreferredSize(new Dimension(700, 50));
        labelDelete.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPanel.add(labelDelete);
        mainPanel.add(bucketDelete);
        mainPanel.add(labelRegion);
        cb2.setPreferredSize(new Dimension(200,50));
        mainPanel.add(cb2);
        buttonDelete.setPreferredSize(new Dimension(200, 50));
        buttonDelete.setBackground(Color.orange);
        mainPanel.add(buttonDelete);
        add(mainPanel);

        //delete object
        labelObj.setPreferredSize(new Dimension(700, 50));
        labelObj.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPanel.add(labelObj);
        mainPanel.add(bucketObj);
        labelDelBuck.setPreferredSize(new Dimension(700,50));
        labelDelBuck.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPanel.add(labelDelBuck);
        mainPanel.add(deleteBuckField);
        mainPanel.add(labelRegion);
        cb.setPreferredSize(new Dimension(200,50));
        mainPanel.add(cb);
        buttonObj.setPreferredSize(new Dimension(200,50));
        buttonObj.setBackground(Color.orange);
        mainPanel.add(buttonObj);
        add(mainPanel);

        //upload object
        labelObjectName.setPreferredSize(new Dimension(700,50));
        labelObjectName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPanel.add(labelObjectName);
        mainPanel.add(fieldUploadObject);
        labelUploadBuck.setPreferredSize(new Dimension(700,50));
        labelUploadBuck.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPanel.add(labelUploadBuck);
        mainPanel.add(fieldUploadBuck);
        labelFilePath.setPreferredSize(new Dimension(700,50));
        labelFilePath.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPanel.add(labelFilePath);
        mainPanel.add(fieldUploadFilePath);
        labelRegion.setPreferredSize(new Dimension(700,50));
        labelRegion.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPanel.add(labelRegion);
        cb3.setPreferredSize(new Dimension(200,50));
        mainPanel.add(cb3);
        buttonUpload.setPreferredSize(new Dimension(200,50));
        buttonUpload.setBackground(Color.orange);
        mainPanel.add(buttonUpload);
        buttonDownload.setPreferredSize(new Dimension(200,50));
        buttonDownload.setBackground(Color.orange);
        mainPanel.add(buttonDownload);


        //check bucket region
        //labelBucketRegion.setPreferredSize(new Dimension(700,50));
        //mainPanel.add(labelBucketRegion);
        labelRegion.setPreferredSize(new Dimension(700,50));
        labelRegion.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPanel.add(labelRegion);
        cb4.setPreferredSize(new Dimension(200,50));
        mainPanel.add(cb4);
        buttonCheckRegion.setPreferredSize(new Dimension(200,50));
        buttonCheckRegion.setBackground(Color.orange);
        mainPanel.add(buttonCheckRegion);
        areaBucket.setPreferredSize(new Dimension(700, 100));
        areaBucket.setEditable(false);
        areaBucket.setVisible(true);
        areaBucket.setBackground(Color.lightGray);
        mainPanel.add(areaBucket);

        //download objects


        buttonCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buckName=bucket.getText();
                String region= cb1.getSelectedItem().toString();
                callObj.createBucketNewRegion(buckName,region);
            }
        });


        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buckName=bucketDelete.getText();
                String region=cb2.getSelectedItem().toString();
                callObj.deleteBucket(buckName,region);
            }
        });


        buttonObj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String objName= bucketObj.getText();
                String buckName=deleteBuckField.getText();
                String region=cb.getSelectedItem().toString();
                try {
                    callObj.deleteObject(buckName,objName,region);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });

        buttonUpload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String objName=fieldUploadObject.getText();
                String buckName=fieldUploadBuck.getText();
                String filePath=fieldUploadFilePath.getText();
                String region=cb3.getSelectedItem().toString();
                callObj.uploadObject(buckName, objName, filePath, region);
            }
        });

        buttonDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String objName=fieldUploadObject.getText();
                String buckName=fieldUploadBuck.getText();
                String filePath=fieldUploadFilePath.getText();
                String region=cb3.getSelectedItem().toString();
                callObj.downloadObject(buckName, objName, filePath, region);
            }
        });

        buttonCheckRegion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String region=cb4.getSelectedItem().toString();
                String regionList=callObj.checkBucketRegion(region);
                areaBucket.append(regionList);
                System.out.println(regionList);
            }
        });
    }

    public static void main(String[] args)
    {
        new GUI();
    }
}
