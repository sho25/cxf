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
name|provider
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
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|MediaType
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
name|MessageBodyReader
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
name|MessageBodyWriter
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
name|fortest
operator|.
name|AegisTestBean
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
name|impl
operator|.
name|MetadataMap
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
name|resources
operator|.
name|CollectionsResource
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
name|resources
operator|.
name|ManyTags
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
name|resources
operator|.
name|TagVO
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
name|resources
operator|.
name|Tags
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
name|AegisJSONProviderTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testIsWriteable
parameter_list|()
block|{
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|AegisJSONProvider
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isWriteable
argument_list|(
name|AegisTestBean
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsReadable
parameter_list|()
block|{
name|MessageBodyReader
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|AegisJSONProvider
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isReadable
argument_list|(
name|AegisTestBean
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadFrom
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestRead
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadFromNoMap
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestRead
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|void
name|doTestRead
parameter_list|(
name|boolean
name|setNsMap
parameter_list|)
throws|throws
name|Exception
block|{
name|AegisJSONProvider
name|p
init|=
operator|new
name|AegisJSONProvider
argument_list|()
decl_stmt|;
name|AbstractAegisProvider
operator|.
name|clearContexts
argument_list|()
expr_stmt|;
if|if
condition|(
name|setNsMap
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|namespaceMap
operator|.
name|put
argument_list|(
literal|"http://fortest.jaxrs.cxf.apache.org"
argument_list|,
literal|"ns1"
argument_list|)
expr_stmt|;
name|namespaceMap
operator|.
name|put
argument_list|(
literal|"http://www.w3.org/2001/XMLSchema-instance"
argument_list|,
literal|"xsi"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setNamespaceMap
argument_list|(
name|namespaceMap
argument_list|)
expr_stmt|;
block|}
name|String
name|data
init|=
literal|"{\"ns1.AegisTestBean\":{\"@xsi.type\":\"ns1:AegisTestBean\","
operator|+
literal|"\"ns1.boolValue\":true,\"ns1.strValue\":\"hovercraft\"}}"
decl_stmt|;
name|byte
index|[]
name|simpleBytes
init|=
name|data
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
decl_stmt|;
name|Object
name|beanObject
init|=
name|p
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|AegisTestBean
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|simpleBytes
argument_list|)
argument_list|)
decl_stmt|;
name|AegisTestBean
name|bean
init|=
operator|(
name|AegisTestBean
operator|)
name|beanObject
decl_stmt|;
name|assertEquals
argument_list|(
literal|"hovercraft"
argument_list|,
name|bean
operator|.
name|getStrValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|bean
operator|.
name|getBoolValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteToWithXsiType
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|data
init|=
literal|"{\"ns1.AegisTestBean\":{\"@xsi.type\":\"ns1:AegisTestBean\","
operator|+
literal|"\"ns1.boolValue\":true,\"ns1.strValue\":\"hovercraft\"}}"
decl_stmt|;
name|doTestWriteTo
argument_list|(
name|data
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteToWithXsiTypeNoNamespaces
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|data
init|=
literal|"{\"ns1.AegisTestBean\":{\"@xsi.type\":\"ns1:AegisTestBean\","
operator|+
literal|"\"ns1.boolValue\":true,\"ns1.strValue\":\"hovercraft\"}}"
decl_stmt|;
name|doTestWriteTo
argument_list|(
name|data
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteToWithoutXsiType
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|data
init|=
literal|"{\"ns1.AegisTestBean\":{"
operator|+
literal|"\"ns1.boolValue\":true,\"ns1.strValue\":\"hovercraft\"}}"
decl_stmt|;
name|doTestWriteTo
argument_list|(
name|data
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestWriteTo
parameter_list|(
name|String
name|data
parameter_list|,
name|boolean
name|writeXsi
parameter_list|,
name|boolean
name|setNsMap
parameter_list|)
throws|throws
name|Exception
block|{
name|AegisJSONProvider
name|p
init|=
operator|new
name|AegisJSONProvider
argument_list|()
decl_stmt|;
name|AbstractAegisProvider
operator|.
name|clearContexts
argument_list|()
expr_stmt|;
name|p
operator|.
name|setWriteXsiType
argument_list|(
name|writeXsi
argument_list|)
expr_stmt|;
if|if
condition|(
name|setNsMap
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|namespaceMap
operator|.
name|put
argument_list|(
literal|"http://fortest.jaxrs.cxf.apache.org"
argument_list|,
literal|"ns1"
argument_list|)
expr_stmt|;
name|namespaceMap
operator|.
name|put
argument_list|(
literal|"http://www.w3.org/2001/XMLSchema-instance"
argument_list|,
literal|"xsi"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setNamespaceMap
argument_list|(
name|namespaceMap
argument_list|)
expr_stmt|;
block|}
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|AegisTestBean
name|bean
init|=
operator|new
name|AegisTestBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setBoolValue
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setStrValue
argument_list|(
literal|"hovercraft"
argument_list|)
expr_stmt|;
name|p
operator|.
name|writeTo
argument_list|(
name|bean
argument_list|,
operator|(
name|Class
operator|)
name|AegisTestBean
operator|.
name|class
argument_list|,
name|AegisTestBean
operator|.
name|class
argument_list|,
name|AegisTestBean
operator|.
name|class
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|os
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|String
name|json
init|=
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|data
argument_list|,
name|json
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
name|writeCollection
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"{\"ns1.ArrayOfAegisTestBean\":{\"@xsi.type\":\"ns1:ArrayOfAegisTestBean\","
operator|+
literal|"\"ns1.AegisTestBean\":{\"@xsi.type\":\"ns1:AegisTestBean\",\"ns1.boolValue\":true,"
operator|+
literal|"\"ns1.strValue\":\"hovercraft\"}}}"
argument_list|,
name|json
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|writeCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|AegisJSONProvider
name|p
init|=
operator|new
name|AegisJSONProvider
argument_list|()
decl_stmt|;
name|AbstractAegisProvider
operator|.
name|clearContexts
argument_list|()
expr_stmt|;
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|AegisTestBean
name|bean
init|=
operator|new
name|AegisTestBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setBoolValue
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setStrValue
argument_list|(
literal|"hovercraft"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|AegisTestBean
argument_list|>
name|beans
init|=
operator|new
name|ArrayList
argument_list|<
name|AegisTestBean
argument_list|>
argument_list|()
decl_stmt|;
name|beans
operator|.
name|add
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|Method
name|m
init|=
name|CollectionsResource
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"getAegisBeans"
argument_list|,
operator|new
name|Class
index|[]
block|{}
argument_list|)
decl_stmt|;
name|p
operator|.
name|writeTo
argument_list|(
name|beans
argument_list|,
operator|(
name|Class
operator|)
name|m
operator|.
name|getReturnType
argument_list|()
argument_list|,
name|m
operator|.
name|getGenericReturnType
argument_list|()
argument_list|,
name|AegisTestBean
operator|.
name|class
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|os
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
return|return
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
literal|"utf-8"
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testReadCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
name|writeCollection
argument_list|()
decl_stmt|;
name|byte
index|[]
name|simpleBytes
init|=
name|json
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
decl_stmt|;
name|Method
name|m
init|=
name|CollectionsResource
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"getAegisBeans"
argument_list|,
operator|new
name|Class
index|[]
block|{}
argument_list|)
decl_stmt|;
name|AegisJSONProvider
name|p
init|=
operator|new
name|AegisJSONProvider
argument_list|()
decl_stmt|;
name|Object
name|beanObject
init|=
name|p
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|m
operator|.
name|getReturnType
argument_list|()
argument_list|,
name|m
operator|.
name|getGenericReturnType
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|simpleBytes
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|AegisTestBean
argument_list|>
name|list
init|=
operator|(
name|List
operator|)
name|beanObject
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|AegisTestBean
name|bean
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"hovercraft"
argument_list|,
name|bean
operator|.
name|getStrValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|bean
operator|.
name|getBoolValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testManyTags
parameter_list|()
throws|throws
name|Exception
block|{
name|AegisJSONProvider
name|p
init|=
operator|new
name|AegisJSONProvider
argument_list|()
decl_stmt|;
name|p
operator|.
name|setWriteXsiType
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|AbstractAegisProvider
operator|.
name|clearContexts
argument_list|()
expr_stmt|;
name|p
operator|.
name|setSerializeAsArray
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Tags
name|tags
init|=
operator|new
name|Tags
argument_list|()
decl_stmt|;
name|tags
operator|.
name|addTag
argument_list|(
name|createTag
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|ManyTags
name|many
init|=
operator|new
name|ManyTags
argument_list|()
decl_stmt|;
name|many
operator|.
name|setTags
argument_list|(
name|tags
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|p
operator|.
name|writeTo
argument_list|(
name|many
argument_list|,
operator|(
name|Class
operator|)
name|ManyTags
operator|.
name|class
argument_list|,
name|ManyTags
operator|.
name|class
argument_list|,
name|ManyTags
operator|.
name|class
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|os
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|data1
init|=
literal|"{\"ns1.ManyTags\":[{\"ns1.tags\":[{},{\"ns1.TagVO\""
operator|+
literal|":{\"ns1.group\":\"b\",\"ns1.name\":\"a\"}}]}]}"
decl_stmt|;
name|String
name|data2
init|=
literal|"{\"ns1.ManyTags\":[{\"ns1.tags\":[{},{\"ns1.TagVO\""
operator|+
literal|":{\"ns1.name\":\"a\",\"ns1.group\":\"b\"}}]}]}"
decl_stmt|;
name|assertTrue
argument_list|(
name|data1
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
name|data2
operator|.
name|equals
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|TagVO
name|createTag
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|group
parameter_list|)
block|{
return|return
operator|new
name|TagVO
argument_list|(
name|name
argument_list|,
name|group
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testReadWriteComplexMap
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|AegisTestBean
argument_list|,
name|AegisSuperBean
argument_list|>
name|testMap
init|=
operator|new
name|HashMap
argument_list|<
name|AegisTestBean
argument_list|,
name|AegisSuperBean
argument_list|>
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|InterfaceWithMap
argument_list|>
name|iwithMapClass
init|=
name|InterfaceWithMap
operator|.
name|class
decl_stmt|;
name|Method
name|method
init|=
name|iwithMapClass
operator|.
name|getMethod
argument_list|(
literal|"mapFunction"
argument_list|)
decl_stmt|;
name|Type
name|mapType
init|=
name|method
operator|.
name|getGenericReturnType
argument_list|()
decl_stmt|;
name|AegisTestBean
name|bean
init|=
operator|new
name|AegisTestBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setBoolValue
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setStrValue
argument_list|(
literal|"hovercraft"
argument_list|)
expr_stmt|;
name|AegisSuperBean
name|bean2
init|=
operator|new
name|AegisSuperBean
argument_list|()
decl_stmt|;
name|bean2
operator|.
name|setBoolValue
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|bean2
operator|.
name|setStrValue
argument_list|(
literal|"hovercraft2"
argument_list|)
expr_stmt|;
name|testMap
operator|.
name|put
argument_list|(
name|bean
argument_list|,
name|bean2
argument_list|)
expr_stmt|;
name|AegisJSONProvider
name|writer
init|=
operator|new
name|AegisJSONProvider
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|namespaceMap
operator|.
name|put
argument_list|(
literal|"urn:org.apache.cxf.aegis.types"
argument_list|,
literal|"ns1"
argument_list|)
expr_stmt|;
name|namespaceMap
operator|.
name|put
argument_list|(
literal|"http://fortest.jaxrs.cxf.apache.org"
argument_list|,
literal|"ns2"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|setNamespaceMap
argument_list|(
name|namespaceMap
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|writer
operator|.
name|writeTo
argument_list|(
name|testMap
argument_list|,
name|testMap
operator|.
name|getClass
argument_list|()
argument_list|,
name|mapType
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|os
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|String
name|xml
init|=
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
literal|"{\"ns1.AegisTestBean2AegisSuperBeanMap\":{\"@xsi.type\":"
operator|+
literal|"\"ns1:AegisTestBean2AegisSuperBeanMap\",\"ns1.entry\":{\"ns1.key\":{\"@xsi.type\":\"ns1:"
operator|+
literal|"AegisTestBean\",\"ns2.boolValue\":true,\"ns2.strValue\":\"hovercraft\"},\"ns1.value\":"
operator|+
literal|"{\"@xsi.type\":\"ns1:AegisSuperBean\",\"ns2.boolValue\":true,"
operator|+
literal|"\"ns2.strValue\":\"hovercraft2\"}}}}"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|xml
argument_list|)
expr_stmt|;
name|AegisJSONProvider
name|reader
init|=
operator|new
name|AegisJSONProvider
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap2
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|namespaceMap2
operator|.
name|put
argument_list|(
literal|"urn:org.apache.cxf.aegis.types"
argument_list|,
literal|"ns1"
argument_list|)
expr_stmt|;
name|namespaceMap2
operator|.
name|put
argument_list|(
literal|"http://fortest.jaxrs.cxf.apache.org"
argument_list|,
literal|"ns2"
argument_list|)
expr_stmt|;
name|reader
operator|.
name|setNamespaceMap
argument_list|(
name|namespaceMap2
argument_list|)
expr_stmt|;
name|byte
index|[]
name|simpleBytes
init|=
name|xml
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
decl_stmt|;
name|Object
name|beanObject
init|=
name|reader
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|Map
operator|.
name|class
argument_list|,
name|mapType
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|simpleBytes
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|AegisTestBean
argument_list|,
name|AegisSuperBean
argument_list|>
name|map2
init|=
operator|(
name|Map
operator|)
name|beanObject
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|AegisTestBean
argument_list|,
name|AegisSuperBean
argument_list|>
name|entry
init|=
name|map2
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|AegisTestBean
name|bean1
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"hovercraft"
argument_list|,
name|bean1
operator|.
name|getStrValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|bean1
operator|.
name|getBoolValue
argument_list|()
argument_list|)
expr_stmt|;
name|AegisTestBean
name|bean22
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"hovercraft2"
argument_list|,
name|bean22
operator|.
name|getStrValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|bean22
operator|.
name|getBoolValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|AegisSuperBean
extends|extends
name|AegisTestBean
block|{     }
specifier|private
specifier|static
interface|interface
name|InterfaceWithMap
block|{
name|Map
argument_list|<
name|AegisTestBean
argument_list|,
name|AegisSuperBean
argument_list|>
name|mapFunction
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

