package br.com.usasistemas.usaplatform.bizo.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.com.usasistemas.usaplatform.dao.DocumentsFolderDAO;
import br.com.usasistemas.usaplatform.dao.impl.GAEDocumentsFolderDAOImpl;
import br.com.usasistemas.usaplatform.model.data.DocumentsFileData;
import br.com.usasistemas.usaplatform.model.data.DocumentsFolderData;

@RunWith(JUnit4.class)
public class DocumentManagementBOImplTest {
	
	private List<DocumentsFolderData> returnSingleDocumentsFolder() {
		List<DocumentsFolderData> response = new ArrayList<DocumentsFolderData>();
		
		DocumentsFolderData folder1 = new DocumentsFolderData();
		folder1.setId(1L);
		response.add(folder1);
		
		return response;
	}

	private List<DocumentsFileData> returnSingleDocumentsFile() {
		List<DocumentsFileData> documentsFiles = new ArrayList<DocumentsFileData>();

		DocumentsFileData file1 = new DocumentsFileData();
		file1.setId(1L);
		file1.setFolderId(1L);
		documentsFiles.add(file1);
		return documentsFiles;
	}

	@Test
	public void testGetFranchisorDocumentsFoldersForNonFranchisor() {
		
		DocumentManagementBOImpl documentManagement = new DocumentManagementBOImpl();
		DocumentsFolderDAO documentsFolderDAOMock = mock(GAEDocumentsFolderDAOImpl.class);
		documentManagement.setDocumentsFolderDAO(documentsFolderDAOMock);
		
		when(documentsFolderDAOMock.findByFranchisorId(any(Long.class)))
		.thenAnswer(request -> {
			List<DocumentsFolderData> response = new ArrayList<DocumentsFolderData>();
			
			DocumentsFolderData folder1 = new DocumentsFolderData();
			folder1.setId(1L);
			response.add(folder1);
			
			DocumentsFolderData folder2 = new DocumentsFolderData();
			folder2.setId(2L);
			response.add(folder2);
			
			return response;
		});

		List<DocumentsFileData> documentsFiles = returnSingleDocumentsFile();
		
		//Non Franchisor
		List<DocumentsFolderData> folders = documentManagement.getFranchisorDocumentsFolders(1L, 2L, documentsFiles);
		
		assertEquals(1, folders.size());
	}

	@Test
	public void testGetFranchisorDocumentsFoldersForFranchisor() {
		
		DocumentManagementBOImpl documentManagement = new DocumentManagementBOImpl();
		DocumentsFolderDAO documentsFolderDAOMock = mock(GAEDocumentsFolderDAOImpl.class);
		documentManagement.setDocumentsFolderDAO(documentsFolderDAOMock);
		
		when(documentsFolderDAOMock.findByFranchisorId(any(Long.class)))
		.thenAnswer(request -> {
			List<DocumentsFolderData> response = new ArrayList<DocumentsFolderData>();
			
			DocumentsFolderData folder1 = new DocumentsFolderData();
			folder1.setId(1L);
			response.add(folder1);
			
			DocumentsFolderData folder2 = new DocumentsFolderData();
			folder2.setId(2L);
			response.add(folder2);
			
			return response;
		});

		List<DocumentsFileData> documentsFiles = returnSingleDocumentsFile();
		
		//Non Franchisor
		List<DocumentsFolderData> folders = documentManagement.getFranchisorDocumentsFolders(1L, 1L, documentsFiles);
		
		assertEquals(2, folders.size());
	}

	@Test
	public void testGetFranchisorDocumentsFoldersForNonFranchisorWithEmptyFolders() {
		
		DocumentManagementBOImpl documentManagement = new DocumentManagementBOImpl();
		DocumentsFolderDAO documentsFolderDAOMock = mock(GAEDocumentsFolderDAOImpl.class);
		documentManagement.setDocumentsFolderDAO(documentsFolderDAOMock);
		
		when(documentsFolderDAOMock.findByFranchisorId(any(Long.class)))
		.thenAnswer(request -> {
			//Return empty folders
			return new ArrayList<DocumentsFolderData>();
		});

		List<DocumentsFileData> documentsFiles = returnSingleDocumentsFile();
		
		//Non Franchisor with empty folders
		List<DocumentsFolderData> folders = documentManagement.getFranchisorDocumentsFolders(1L, 2L, documentsFiles);
		
		assertEquals(0, folders.size());
	}

	@Test
	public void testGetFranchisorDocumentsFoldersForNonFranchisorWithNullFolders() {
		
		DocumentManagementBOImpl documentManagement = new DocumentManagementBOImpl();
		DocumentsFolderDAO documentsFolderDAOMock = mock(GAEDocumentsFolderDAOImpl.class);
		documentManagement.setDocumentsFolderDAO(documentsFolderDAOMock);
		
		when(documentsFolderDAOMock.findByFranchisorId(any(Long.class)))
		.thenAnswer(request -> {
			//Return null folders
			return null;
		});

		List<DocumentsFileData> documentsFiles = returnSingleDocumentsFile();
		
		//Non Franchisor with null folders
		List<DocumentsFolderData> folders = documentManagement.getFranchisorDocumentsFolders(1L, 2L, documentsFiles);
		
		assertEquals(0, folders.size());
	}

	@Test
	public void testGetFranchisorDocumentsFoldersForNonFranchisorWithEmptyFiles() {
		
		DocumentManagementBOImpl documentManagement = new DocumentManagementBOImpl();
		DocumentsFolderDAO documentsFolderDAOMock = mock(GAEDocumentsFolderDAOImpl.class);
		documentManagement.setDocumentsFolderDAO(documentsFolderDAOMock);
		
		when(documentsFolderDAOMock.findByFranchisorId(any(Long.class)))
		.thenAnswer(request -> {
			return returnSingleDocumentsFolder();
		});

		//Empty files
		List<DocumentsFileData> documentsFiles = new ArrayList<DocumentsFileData>();
		
		//Non franchisor with empty files
		List<DocumentsFolderData> folders = documentManagement.getFranchisorDocumentsFolders(1L, 2L, documentsFiles);
		
		assertEquals(0, folders.size());
	}

	@Test
	public void testGetFranchisorDocumentsFoldersForNonFranchisorWithNullFiles() {
		
		DocumentManagementBOImpl documentManagement = new DocumentManagementBOImpl();
		DocumentsFolderDAO documentsFolderDAOMock = mock(GAEDocumentsFolderDAOImpl.class);
		documentManagement.setDocumentsFolderDAO(documentsFolderDAOMock);
		
		when(documentsFolderDAOMock.findByFranchisorId(any(Long.class)))
		.thenAnswer(request -> {
			return returnSingleDocumentsFolder();
		});

		//Non franchisor with null files
		List<DocumentsFolderData> folders = documentManagement.getFranchisorDocumentsFolders(1L, 2L, null);
		
		assertEquals(0, folders.size());
	}

}
