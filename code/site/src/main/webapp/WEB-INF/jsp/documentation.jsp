<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="maincontent">

	<h2>Reference/Configuration properties</h2>
	
	Each of the indexables has properties that can be set, these are all described below. In the reference configuration there are real 
	examples of each type of configuration and source of data, file system, database etc. Along with the configuration is the explainations 
	on each of the components, i.e. in the configuration files. Please have a look at the files, copy a configuration that best suits your 
	needs and modify as required.
	<br><br>

	<table>
		<tr>
			<th>Name</th>
			<th>Property</th>
			<th>Lucene field</th>
			<th>Description</th>
		</tr>
		<tr>
			<td>Indexable</td>
			<td>name<br>address<br>stored<br>analyzed<br>vectored<br>maxExceptions<br></td>
			<td>false<br>false<br>false<br>false<br>false<br>false<br></td>
			<td nowrap="nowrap">The name of this indexable<br>Whether
				this is a geospatial address field<br>Whether this value should
				be stored in the index<br>Whether this field should be analyzed
				for stemming and so on<br>Whether this field should be vectored
				in the index<br>This is the maximum exceptions during indexing
				before the indexing is stopped<br></td>
		</tr>
		<tr>
			<td>IndexableFileSystemLog</td>
			<td>path<br>fileFieldName<br>pathFieldName<br>lineFieldName<br>contentFieldName<br>name<br>address<br>stored<br>analyzed<br>vectored<br>maxExceptions<br></td>
			<td>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br></td>
			<td nowrap="nowrap">This is the path to the log folder<br>This
				is the file name field in the Lucene index<br>This is the path
				name field in the Lucene index<br>This is the line number field
				in the Lucene index<br>This is the content field in the Lucene
				index<br>The name of this indexable<br>Whether this is a
				geospatial address field<br>Whether this value should be stored
				in the index<br>Whether this field should be analyzed for
				stemming and so on<br>Whether this field should be vectored in
				the index<br>This is the maximum exceptions during indexing
				before the indexing is stopped<br></td>
		</tr>
		<tr>
			<td>IndexableEmail</td>
			<td>idField<br>titleField<br>contentField<br>mailHost<br>username<br>password<br>port<br>protocol<br>secureSocketLayer<br>name<br>address<br>stored<br>analyzed<br>vectored<br>maxExceptions<br></td>
			<td>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br></td>
			<td nowrap="nowrap">This is the name of the id field in the
				Lucene index<br>This is the name of the title field in the
				Lucene index<br>This is the namd of the content field in the
				Lucene index<br>The url where the mail is hosted, i.e. the Imap
				or Pop server<br>The user name of the mail account<br>The
				password for the mail account<br>The port number of the mail
				account<br>The protocol of the account, Imap or Pop3 for
				example<br>Whether to use SSL for the mail access<br>The
				name of this indexable<br>Whether this is a geospatial address
				field<br>Whether this value should be stored in the index<br>Whether
				this field should be analyzed for stemming and so on<br>Whether
				this field should be vectored in the index<br>This is the
				maximum exceptions during indexing before the indexing is stopped<br>
			</td>
		</tr>
		<tr>
			<td>IndexableFileSystem</td>
			<td>path<br>excludedPattern<br>includedPattern<br>maxReadLength<br>nameFieldName<br>pathFieldName<br>lastModifiedFieldName<br>contentFieldName<br>lengthFieldName<br>batchSize<br>unpackZips<br>name<br>address<br>stored<br>analyzed<br>vectored<br>maxExceptions<br></td>
			<td>false<br>false<br>false<br>false<br>true<br>true<br>true<br>true<br>true<br>true<br>true<br>false<br>false<br>false<br>false<br>false<br>false<br></td>
			<td nowrap="nowrap">This is the path to the folder where the
				files to be indexed are<br>This is a pattern that will be
				applied to the file name and path to exclude resources that are not
				to be indexed<br>This is a pattern that will be applied to the
				name and path to specifically include resources that are to be
				included in the index<br>This is the maximum read length that
				will be read from a file. This is required where files are very
				large and need to be read into memory completely<br>This is the
				file name field in the Lucene index<br>This is the name of the
				path field in the Lucene index<br>This is the name of the last
				modified field in the Lucene index<br>This is the name of the
				content field in the Lucene index<br>This is the name of the
				length field in the Lucene index<br>This is the name of the
				batch size for files, i.e. how many files each thread will batch,
				not read in one shot, typical would be 1000<br>Whether to
				unpack the zip files found, this is deprecated and done
				automatically by reading in the zips and jars<br>The name of
				this indexable<br>Whether this is a geospatial address field<br>Whether
				this value should be stored in the index<br>Whether this field
				should be analyzed for stemming and so on<br>Whether this field
				should be vectored in the index<br>This is the maximum
				exceptions during indexing before the indexing is stopped<br>
			</td>
		</tr>
		<tr>
			<td>IndexableFileSystemCsv</td>
			<td>path<br>excludedPattern<br>includedPattern<br>maxReadLength<br>nameFieldName<br>pathFieldName<br>lastModifiedFieldName<br>contentFieldName<br>lengthFieldName<br>batchSize<br>unpackZips<br>name<br>address<br>stored<br>analyzed<br>vectored<br>maxExceptions<br></td>
			<td>false<br>false<br>false<br>false<br>true<br>true<br>true<br>true<br>true<br>true<br>true<br>false<br>false<br>false<br>false<br>false<br>false<br></td>
			<td nowrap="nowrap">This is the path to the folder where the
				files to be indexed are<br>This is a pattern that will be
				applied to the file name and path to exclude resources that are not
				to be indexed<br>This is a pattern that will be applied to the
				name and path to specifically include resources that are to be
				included in the index<br>This is the maximum read length that
				will be read from a file. This is required where files are very
				large and need to be read into memory completely<br>This is the
				file name field in the Lucene index<br>This is the name of the
				path field in the Lucene index<br>This is the name of the last
				modified field in the Lucene index<br>This is the name of the
				content field in the Lucene index<br>This is the name of the
				length field in the Lucene index<br>This is the name of the
				batch size for files, i.e. how many files each thread will batch,
				not read in one shot, typical would be 1000<br>Whether to
				unpack the zip files found, this is deprecated and done
				automatically by reading in the zips and jars<br>The name of
				this indexable<br>Whether this is a geospatial address field<br>Whether
				this value should be stored in the index<br>Whether this field
				should be analyzed for stemming and so on<br>Whether this field
				should be vectored in the index<br>This is the maximum
				exceptions during indexing before the indexing is stopped<br>
			</td>
		</tr>
		<tr>
			<td>IndexableInternet</td>
			<td>url<br>loginUrl<br>userid<br>password<br>internetBatchSize<br>excludedPattern<br>timeout<br>titleFieldName<br>idFieldName<br>contentFieldName<br>name<br>address<br>stored<br>analyzed<br>vectored<br>maxExceptions<br></td>
			<td>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br></td>
			<td nowrap="nowrap">This is the primary url that will be crawled<br>This
				is the url to the login page if it is a protected site<br>This
				is the userid to login to the site<br>This is the password to
				login to the site<br>This is the size that the batches of urls
				will be per thread<br>This is is a pattern that will be appled
				to exclude any urls, i.e. urls that should not be crawled, like
				confidential pages etc.<br>This is the length of time that the
				crawler will wait for a particular page to be delivered<br>This
				is the name of the title field in the Lucene index<br>This is
				the name of the id field in the Lucene index<br>This is the
				name of the content field int he Lucene index<br>The name of
				this indexable<br>Whether this is a geospatial address field<br>Whether
				this value should be stored in the index<br>Whether this field
				should be analyzed for stemming and so on<br>Whether this field
				should be vectored in the index<br>This is the maximum
				exceptions during indexing before the indexing is stopped<br></td>
		</tr>
		<tr>
			<td>IndexableTable</td>
			<td>predicate<br>primaryTable<br>allColumns<br>name<br>address<br>stored<br>analyzed<br>vectored<br>maxExceptions<br></td>
			<td>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br></td>
			<td nowrap="nowrap">This is a sql predicate, like 'where id &gt;
				1000'<br>This flag for whether the table is primary, i.e. not a
				joined table or a child table in the configuration<br>This flag
				is whether to index all the columns in the database, default is true<br>The
				name of this indexable<br>Whether this is a geospatial address
				field<br>Whether this value should be stored in the index<br>Whether
				this field should be analyzed for stemming and so on<br>Whether
				this field should be vectored in the index<br>This is the
				maximum exceptions during indexing before the indexing is stopped<br>
			</td>
		</tr>
		<tr>
			<td>IndexableColumn</td>
			<td>fieldName<br>name<br>address<br>stored<br>analyzed<br>vectored<br>maxExceptions<br></td>
			<td>true<br>false<br>false<br>false<br>false<br>false<br>false<br></td>
			<td nowrap="nowrap">This is the name of the field in the Lucene
				index<br>The name of this indexable<br>Whether this is a
				geospatial address field<br>Whether this value should be stored
				in the index<br>Whether this field should be analyzed for
				stemming and so on<br>Whether this field should be vectored in
				the index<br>This is the maximum exceptions during indexing
				before the indexing is stopped<br>
			</td>
		</tr>
		<tr>
			<td>IndexableDataSource</td>
			<td>allColumns<br>excludedTablePatterns<br>name<br>address<br>stored<br>analyzed<br>vectored<br>maxExceptions<br></td>
			<td>false<br>false<br>false<br>false<br>false<br>false<br>false<br>false<br></td>
			<td nowrap="nowrap">This flag is whether to index all the
				columns in the database, default is true<br>This is a delimiter
				seperated list of patterns that will exclude tables from being
				indexed<br>The name of this indexable<br>Whether this is a
				geospatial address field<br>Whether this value should be stored
				in the index<br>Whether this field should be analyzed for
				stemming and so on<br>Whether this field should be vectored in
				the index<br>This is the maximum exceptions during indexing
				before the indexing is stopped<br>
			</td>
		</tr>
		<tr>
			<td>IndexableFileSystemWiki</td>
			<td>maxRevisions<br>path<br>excludedPattern<br>includedPattern<br>maxReadLength<br>nameFieldName<br>pathFieldName<br>lastModifiedFieldName<br>contentFieldName<br>lengthFieldName<br>batchSize<br>unpackZips<br>name<br>address<br>stored<br>analyzed<br>vectored<br>maxExceptions<br></td>
			<td>false<br>false<br>false<br>false<br>false<br>true<br>true<br>true<br>true<br>true<br>true<br>true<br>false<br>false<br>false<br>false<br>false<br>false<br></td>
			<td nowrap="nowrap">This is the maximum documents that will be
				read from the source before the indexing terminates<br>This is
				the path to the folder where the files to be indexed are<br>This
				is a pattern that will be applied to the file name and path to
				exclude resources that are not to be indexed<br>This is a
				pattern that will be applied to the name and path to specifically
				include resources that are to be included in the index<br>This
				is the maximum read length that will be read from a file. This is
				required where files are very large and need to be read into memory
				completely<br>This is the file name field in the Lucene index<br>This
				is the name of the path field in the Lucene index<br>This is
				the name of the last modified field in the Lucene index<br>This
				is the name of the content field in the Lucene index<br>This is
				the name of the length field in the Lucene index<br>This is the
				name of the batch size for files, i.e. how many files each thread
				will batch, not read in one shot, typical would be 1000<br>Whether
				to unpack the zip files found, this is deprecated and done
				automatically by reading in the zips and jars<br>The name of
				this indexable<br>Whether this is a geospatial address field<br>Whether
				this value should be stored in the index<br>Whether this field
				should be analyzed for stemming and so on<br>Whether this field
				should be vectored in the index<br>This is the maximum
				exceptions during indexing before the indexing is stopped<br>
			</td>
		</tr>
		<tr>
			<td>IndexableDictionary</td>
			<td>name<br>address<br>stored<br>analyzed<br>vectored<br>maxExceptions<br></td>
			<td>false<br>false<br>false<br>false<br>false<br>false<br></td>
			<td nowrap="nowrap">The name of this indexable<br>Whether
				this is a geospatial address field<br>Whether this value should
				be stored in the index<br>Whether this field should be analyzed
				for stemming and so on<br>Whether this field should be vectored
				in the index<br>This is the maximum exceptions during indexing
				before the indexing is stopped<br></td>
		</tr>
	</table>


</div>