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
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|core
operator|.
name|HttpHeaders
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
name|core
operator|.
name|Link
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
name|core
operator|.
name|MultivaluedMap
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
name|core
operator|.
name|NewCookie
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
name|core
operator|.
name|Response
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
name|core
operator|.
name|Response
operator|.
name|Status
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
name|core
operator|.
name|Response
operator|.
name|StatusType
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
name|utils
operator|.
name|HttpUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|ResponseImplTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testResourceImpl
parameter_list|()
block|{
name|String
name|entity
init|=
literal|"bar"
decl_stmt|;
name|ResponseImpl
name|ri
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|,
name|entity
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong status"
argument_list|,
name|ri
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Wrong entity"
argument_list|,
name|entity
argument_list|,
name|ri
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|meta
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|ri
operator|.
name|addMetadata
argument_list|(
name|meta
argument_list|)
expr_stmt|;
name|ri
operator|.
name|getMetadata
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
literal|"Wrong metadata"
argument_list|,
name|meta
argument_list|,
name|ri
operator|.
name|getMetadata
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Wrong metadata"
argument_list|,
name|meta
argument_list|,
name|ri
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHasEntity
parameter_list|()
block|{
name|assertTrue
argument_list|(
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|,
literal|""
argument_list|)
operator|.
name|hasEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|)
operator|.
name|hasEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStatuInfoForOKStatus
parameter_list|()
block|{
name|StatusType
name|si
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|,
literal|""
argument_list|)
operator|.
name|getStatusInfo
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|si
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|si
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|Family
operator|.
name|SUCCESSFUL
argument_list|,
name|si
operator|.
name|getFamily
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"OK"
argument_list|,
name|si
operator|.
name|getReasonPhrase
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStatuInfoForClientErrorStatus
parameter_list|()
block|{
name|StatusType
name|si
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|400
argument_list|,
literal|""
argument_list|)
operator|.
name|getStatusInfo
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|si
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|400
argument_list|,
name|si
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|Family
operator|.
name|CLIENT_ERROR
argument_list|,
name|si
operator|.
name|getFamily
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bad Request"
argument_list|,
name|si
operator|.
name|getReasonPhrase
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStatuInfoForClientErrorStatus2
parameter_list|()
block|{
name|StatusType
name|si
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|499
argument_list|,
literal|""
argument_list|)
operator|.
name|getStatusInfo
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|si
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|499
argument_list|,
name|si
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|Family
operator|.
name|CLIENT_ERROR
argument_list|,
name|si
operator|.
name|getFamily
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|si
operator|.
name|getReasonPhrase
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHasEntityAfterClose
parameter_list|()
block|{
name|Response
name|r
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"data"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|r
operator|.
name|hasEntity
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|r
operator|.
name|hasEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBufferEntityNoEntity
parameter_list|()
block|{
name|Response
name|r
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|r
operator|.
name|bufferEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHeaderString
parameter_list|()
block|{
name|ResponseImpl
name|ri
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|)
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|meta
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|ri
operator|.
name|addMetadata
argument_list|(
name|meta
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ri
operator|.
name|getHeaderString
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|meta
operator|.
name|putSingle
argument_list|(
literal|"a"
argument_list|,
literal|"aValue"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aValue"
argument_list|,
name|ri
operator|.
name|getHeaderString
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|meta
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|"aValue2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aValue,aValue2"
argument_list|,
name|ri
operator|.
name|getHeaderString
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHeaderStrings
parameter_list|()
block|{
name|ResponseImpl
name|ri
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|)
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|meta
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|meta
operator|.
name|add
argument_list|(
literal|"Set-Cookie"
argument_list|,
name|NewCookie
operator|.
name|valueOf
argument_list|(
literal|"a=b"
argument_list|)
argument_list|)
expr_stmt|;
name|ri
operator|.
name|addMetadata
argument_list|(
name|meta
argument_list|)
expr_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|ri
operator|.
name|getStringHeaders
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|headers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a=b;Version=1"
argument_list|,
name|headers
operator|.
name|getFirst
argument_list|(
literal|"Set-Cookie"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetCookies
parameter_list|()
block|{
name|ResponseImpl
name|ri
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|)
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|meta
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|meta
operator|.
name|add
argument_list|(
literal|"Set-Cookie"
argument_list|,
name|NewCookie
operator|.
name|valueOf
argument_list|(
literal|"a=b"
argument_list|)
argument_list|)
expr_stmt|;
name|meta
operator|.
name|add
argument_list|(
literal|"Set-Cookie"
argument_list|,
name|NewCookie
operator|.
name|valueOf
argument_list|(
literal|"c=d"
argument_list|)
argument_list|)
expr_stmt|;
name|ri
operator|.
name|addMetadata
argument_list|(
name|meta
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|NewCookie
argument_list|>
name|cookies
init|=
name|ri
operator|.
name|getCookies
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cookies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a=b;Version=1"
argument_list|,
name|cookies
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c=d;Version=1"
argument_list|,
name|cookies
operator|.
name|get
argument_list|(
literal|"c"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetContentLength
parameter_list|()
block|{
name|ResponseImpl
name|ri
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|)
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|meta
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|ri
operator|.
name|addMetadata
argument_list|(
name|meta
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|ri
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|meta
operator|.
name|add
argument_list|(
literal|"Content-Length"
argument_list|,
literal|"10"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|ri
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetDate
parameter_list|()
block|{
name|doTestDate
argument_list|(
name|HttpHeaders
operator|.
name|DATE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLastModified
parameter_list|()
block|{
name|doTestDate
argument_list|(
name|HttpHeaders
operator|.
name|LAST_MODIFIED
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|doTestDate
parameter_list|(
name|String
name|dateHeader
parameter_list|)
block|{
name|boolean
name|date
init|=
name|HttpHeaders
operator|.
name|DATE
operator|.
name|equals
argument_list|(
name|dateHeader
argument_list|)
decl_stmt|;
name|ResponseImpl
name|ri
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|)
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|meta
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|meta
operator|.
name|add
argument_list|(
name|dateHeader
argument_list|,
literal|"Tue, 21 Oct 2008 17:00:00 GMT"
argument_list|)
expr_stmt|;
name|ri
operator|.
name|addMetadata
argument_list|(
name|meta
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HttpUtils
operator|.
name|getHttpDate
argument_list|(
literal|"Tue, 21 Oct 2008 17:00:00 GMT"
argument_list|)
argument_list|,
name|date
condition|?
name|ri
operator|.
name|getDate
argument_list|()
else|:
name|ri
operator|.
name|getLastModified
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEntityTag
parameter_list|()
block|{
name|ResponseImpl
name|ri
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|)
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|meta
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|meta
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|ETAG
argument_list|,
literal|"1234"
argument_list|)
expr_stmt|;
name|ri
operator|.
name|addMetadata
argument_list|(
name|meta
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"\"1234\""
argument_list|,
name|ri
operator|.
name|getEntityTag
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLocation
parameter_list|()
block|{
name|ResponseImpl
name|ri
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|)
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|meta
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|meta
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|LOCATION
argument_list|,
literal|"http://localhost:8080"
argument_list|)
expr_stmt|;
name|ri
operator|.
name|addMetadata
argument_list|(
name|meta
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:8080"
argument_list|,
name|ri
operator|.
name|getLocation
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetLanguage
parameter_list|()
block|{
name|ResponseImpl
name|ri
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|)
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|meta
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|meta
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LANGUAGE
argument_list|,
literal|"en-US"
argument_list|)
expr_stmt|;
name|ri
operator|.
name|addMetadata
argument_list|(
name|meta
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"en_US"
argument_list|,
name|ri
operator|.
name|getLanguage
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetMediaType
parameter_list|()
block|{
name|ResponseImpl
name|ri
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|)
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|meta
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|meta
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|ri
operator|.
name|addMetadata
argument_list|(
name|meta
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/xml"
argument_list|,
name|ri
operator|.
name|getMediaType
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetLinks
parameter_list|()
block|{
name|ResponseImpl
name|ri
init|=
operator|new
name|ResponseImpl
argument_list|(
literal|200
argument_list|)
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|meta
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|ri
operator|.
name|addMetadata
argument_list|(
name|meta
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ri
operator|.
name|hasLink
argument_list|(
literal|"next"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ri
operator|.
name|getLink
argument_list|(
literal|"next"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ri
operator|.
name|hasLink
argument_list|(
literal|"prev"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ri
operator|.
name|getLink
argument_list|(
literal|"prev"
argument_list|)
argument_list|)
expr_stmt|;
name|meta
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|LINK
argument_list|,
literal|"<http://next>;rel=next"
argument_list|)
expr_stmt|;
name|meta
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|LINK
argument_list|,
literal|"<http://prev>;rel=prev"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ri
operator|.
name|hasLink
argument_list|(
literal|"next"
argument_list|)
argument_list|)
expr_stmt|;
name|Link
name|next
init|=
name|ri
operator|.
name|getLink
argument_list|(
literal|"next"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|next
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ri
operator|.
name|hasLink
argument_list|(
literal|"prev"
argument_list|)
argument_list|)
expr_stmt|;
name|Link
name|prev
init|=
name|ri
operator|.
name|getLink
argument_list|(
literal|"prev"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|prev
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Link
argument_list|>
name|links
init|=
name|ri
operator|.
name|getLinks
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|links
operator|.
name|contains
argument_list|(
name|next
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|links
operator|.
name|contains
argument_list|(
name|prev
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://next"
argument_list|,
name|next
operator|.
name|getUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"next"
argument_list|,
name|next
operator|.
name|getRel
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://prev"
argument_list|,
name|prev
operator|.
name|getUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"prev"
argument_list|,
name|prev
operator|.
name|getRel
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

