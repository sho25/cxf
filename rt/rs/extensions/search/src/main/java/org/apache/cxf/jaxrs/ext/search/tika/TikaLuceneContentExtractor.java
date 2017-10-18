begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|tika
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|ParamConverterProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|ParamConverterUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|tika
operator|.
name|TikaContentExtractor
operator|.
name|TikaContent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|DoubleField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Field
operator|.
name|Store
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|FloatField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|IntField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|LongField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|StringField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|TextField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|metadata
operator|.
name|Metadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|parser
operator|.
name|Parser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|sax
operator|.
name|ToTextContentHandler
import|;
end_import

begin_class
specifier|public
class|class
name|TikaLuceneContentExtractor
block|{
specifier|private
specifier|final
name|LuceneDocumentMetadata
name|defaultDocumentMetadata
decl_stmt|;
specifier|private
specifier|final
name|TikaContentExtractor
name|extractor
decl_stmt|;
comment|/**      * Create new Tika-based content extractor using the provided parser instance.      * @param parser parser instance      */
specifier|public
name|TikaLuceneContentExtractor
parameter_list|(
specifier|final
name|Parser
name|parser
parameter_list|)
block|{
name|this
argument_list|(
name|parser
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create new Tika-based content extractor using the provided parser instance and      * optional media type validation. If validation is enabled, the implementation      * will try to detect the media type of the input and validate it against media typesthis.contentFieldName      * supported by the parser.      * @param parser parser instance      * @param validateMediaType enabled or disable media type validation      */
specifier|public
name|TikaLuceneContentExtractor
parameter_list|(
specifier|final
name|Parser
name|parser
parameter_list|,
specifier|final
name|boolean
name|validateMediaType
parameter_list|)
block|{
name|this
argument_list|(
name|parser
argument_list|,
name|validateMediaType
argument_list|,
operator|new
name|LuceneDocumentMetadata
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create new Tika-based content extractor using the provided parser instance and      * optional media type validation. If validation is enabled, the implementation      * will try to detect the media type of the input and validate it against media types      * supported by the parser.      * @param parser parser instancethis.contentFieldName      * @param documentMetadata documentMetadata      */
specifier|public
name|TikaLuceneContentExtractor
parameter_list|(
specifier|final
name|Parser
name|parser
parameter_list|,
specifier|final
name|LuceneDocumentMetadata
name|documentMetadata
parameter_list|)
block|{
name|this
argument_list|(
name|parser
argument_list|,
literal|false
argument_list|,
name|documentMetadata
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create new Tika-based content extractor using the provided parser instance and      * optional media type validation. If validation is enabled, the implementation      * will try to detect the media type of the input and validate it against media types      * supported by the parser.      * @param parser parser instancethis.contentFieldName      * @param validateMediaType enabled or disable media type validation      * @param documentMetadata documentMetadata      */
specifier|public
name|TikaLuceneContentExtractor
parameter_list|(
specifier|final
name|Parser
name|parser
parameter_list|,
specifier|final
name|boolean
name|validateMediaType
parameter_list|,
specifier|final
name|LuceneDocumentMetadata
name|documentMetadata
parameter_list|)
block|{
name|this
operator|.
name|extractor
operator|=
operator|new
name|TikaContentExtractor
argument_list|(
name|parser
argument_list|,
name|validateMediaType
argument_list|)
expr_stmt|;
name|this
operator|.
name|defaultDocumentMetadata
operator|=
name|documentMetadata
expr_stmt|;
block|}
comment|/**      * Create new Tika-based content extractor using the provided parser instance and      * optional media type validation. If validation is enabled, the implementation      * will try to detect the media type of the input and validate it against media types      * supported by the parser.      * @param parser parser instancethis.contentFieldName      * @param validateMediaType enabled or disable media type validation      * @param documentMetadata documentMetadata      */
specifier|public
name|TikaLuceneContentExtractor
parameter_list|(
specifier|final
name|List
argument_list|<
name|Parser
argument_list|>
name|parsers
parameter_list|,
specifier|final
name|LuceneDocumentMetadata
name|documentMetadata
parameter_list|)
block|{
name|this
operator|.
name|extractor
operator|=
operator|new
name|TikaContentExtractor
argument_list|(
name|parsers
argument_list|)
expr_stmt|;
name|this
operator|.
name|defaultDocumentMetadata
operator|=
name|documentMetadata
expr_stmt|;
block|}
comment|/**      * Extract the content and metadata from the input stream. Depending on media type validation,      * the detector could be run against input stream in order to ensure that parser supports this      * type of content.      * @param in input stream to extract the content and metadata from      * @return the extracted document or null if extraction is not possible or was unsuccessful      */
specifier|public
name|Document
name|extract
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|)
block|{
return|return
name|extractAll
argument_list|(
name|in
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Extract the content and metadata from the input stream. Depending on media type validation,      * the detector could be run against input stream in order to ensure that parser supports this      * type of content.      * @param in input stream to extract the content and metadata from      * @param documentMetadata documentMetadata      * @return the extracted document or null if extraction is not possible or was unsuccessful      */
specifier|public
name|Document
name|extract
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|,
specifier|final
name|LuceneDocumentMetadata
name|documentMetadata
parameter_list|)
block|{
return|return
name|extractAll
argument_list|(
name|in
argument_list|,
name|documentMetadata
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Extract the content only from the input stream. Depending on media type validation,      * the detector could be run against input stream in order to ensure that parser supports this      * type of content.      * @param in input stream to extract the content from      * @return the extracted document or null if extraction is not possible or was unsuccessful      */
specifier|public
name|Document
name|extractContent
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|)
block|{
return|return
name|extractAll
argument_list|(
name|in
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Extract the metadata only from the input stream. Depending on media type validation,      * the detector could be run against input stream in order to ensure that parser supports this      * type of content.      * @param in input stream to extract the metadata from      * @return the extracted document or null if extraction is not possible or was unsuccessful      */
specifier|public
name|Document
name|extractMetadata
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|)
block|{
return|return
name|extractAll
argument_list|(
name|in
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Extract the metadata only from the input stream. Depending on media type validation,      * the detector could be run against input stream in order to ensure that parser supports this      * type of content.      * @param in input stream to extract the metadata from      * @param documentMetadata documentMetadata      * @return the extracted document or null if extraction is not possible or was unsuccessful      */
specifier|public
name|Document
name|extractMetadata
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|,
specifier|final
name|LuceneDocumentMetadata
name|documentMetadata
parameter_list|)
block|{
return|return
name|extractAll
argument_list|(
name|in
argument_list|,
name|documentMetadata
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|private
name|Document
name|extractAll
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|,
name|LuceneDocumentMetadata
name|documentMetadata
parameter_list|,
name|boolean
name|extractContent
parameter_list|,
name|boolean
name|extractMetadata
parameter_list|)
block|{
name|TikaContent
name|content
init|=
name|extractor
operator|.
name|extract
argument_list|(
name|in
argument_list|,
name|extractContent
condition|?
operator|new
name|ToTextContentHandler
argument_list|()
else|:
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|Document
name|document
init|=
operator|new
name|Document
argument_list|()
decl_stmt|;
if|if
condition|(
name|documentMetadata
operator|==
literal|null
condition|)
block|{
name|documentMetadata
operator|=
name|defaultDocumentMetadata
expr_stmt|;
block|}
if|if
condition|(
name|content
operator|.
name|getContent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|document
operator|.
name|add
argument_list|(
name|getContentField
argument_list|(
name|documentMetadata
argument_list|,
name|content
operator|.
name|getContent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|extractMetadata
condition|)
block|{
name|Metadata
name|metadata
init|=
name|content
operator|.
name|getMetadata
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|property
range|:
name|metadata
operator|.
name|names
argument_list|()
control|)
block|{
name|document
operator|.
name|add
argument_list|(
name|getField
argument_list|(
name|documentMetadata
argument_list|,
name|property
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|property
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|documentMetadata
operator|.
name|getSource
argument_list|()
argument_list|)
condition|)
block|{
name|document
operator|.
name|add
argument_list|(
operator|new
name|StringField
argument_list|(
name|documentMetadata
operator|.
name|getSourceFieldName
argument_list|()
argument_list|,
name|documentMetadata
operator|.
name|getSource
argument_list|()
argument_list|,
name|Store
operator|.
name|YES
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|document
return|;
block|}
specifier|private
specifier|static
name|Field
name|getContentField
parameter_list|(
specifier|final
name|LuceneDocumentMetadata
name|documentMetadata
parameter_list|,
specifier|final
name|String
name|content
parameter_list|)
block|{
return|return
operator|new
name|TextField
argument_list|(
name|documentMetadata
operator|.
name|getContentFieldName
argument_list|()
argument_list|,
name|content
argument_list|,
name|Store
operator|.
name|YES
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Field
name|getField
parameter_list|(
specifier|final
name|LuceneDocumentMetadata
name|documentMetadata
parameter_list|,
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|String
name|value
parameter_list|)
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|documentMetadata
operator|.
name|getFieldType
argument_list|(
name|name
argument_list|)
decl_stmt|;
specifier|final
name|ParamConverterProvider
name|provider
init|=
name|documentMetadata
operator|.
name|getFieldTypeConverter
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|Number
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
if|if
condition|(
name|Double
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
operator|new
name|DoubleField
argument_list|(
name|name
argument_list|,
name|ParamConverterUtils
operator|.
name|getValue
argument_list|(
name|Double
operator|.
name|class
argument_list|,
name|provider
argument_list|,
name|value
argument_list|)
argument_list|,
name|Store
operator|.
name|YES
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|Float
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
operator|new
name|FloatField
argument_list|(
name|name
argument_list|,
name|ParamConverterUtils
operator|.
name|getValue
argument_list|(
name|Float
operator|.
name|class
argument_list|,
name|provider
argument_list|,
name|value
argument_list|)
argument_list|,
name|Store
operator|.
name|YES
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|Long
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
operator|new
name|LongField
argument_list|(
name|name
argument_list|,
name|ParamConverterUtils
operator|.
name|getValue
argument_list|(
name|Long
operator|.
name|class
argument_list|,
name|provider
argument_list|,
name|value
argument_list|)
argument_list|,
name|Store
operator|.
name|YES
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|Integer
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|Byte
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
operator|new
name|IntField
argument_list|(
name|name
argument_list|,
name|ParamConverterUtils
operator|.
name|getValue
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|provider
argument_list|,
name|value
argument_list|)
argument_list|,
name|Store
operator|.
name|YES
argument_list|)
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|Date
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
specifier|final
name|Date
name|date
init|=
name|ParamConverterUtils
operator|.
name|getValue
argument_list|(
name|Date
operator|.
name|class
argument_list|,
name|provider
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|Field
name|field
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|date
operator|!=
literal|null
condition|)
block|{
name|field
operator|=
operator|new
name|StringField
argument_list|(
name|name
argument_list|,
name|ParamConverterUtils
operator|.
name|getString
argument_list|(
name|Date
operator|.
name|class
argument_list|,
name|provider
argument_list|,
name|date
argument_list|)
argument_list|,
name|Store
operator|.
name|YES
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|field
operator|=
operator|new
name|StringField
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|Store
operator|.
name|YES
argument_list|)
expr_stmt|;
block|}
return|return
name|field
return|;
block|}
block|}
return|return
operator|new
name|StringField
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|Store
operator|.
name|YES
argument_list|)
return|;
block|}
block|}
end_class

end_unit

