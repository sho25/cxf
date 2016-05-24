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
name|ws
operator|.
name|transfer
operator|.
name|integration
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|endpoint
operator|.
name|Server
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
name|helpers
operator|.
name|DOMUtils
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
name|ws
operator|.
name|addressing
operator|.
name|ReferenceParametersType
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
name|ws
operator|.
name|transfer
operator|.
name|Put
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
name|ws
operator|.
name|transfer
operator|.
name|PutResponse
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
name|ws
operator|.
name|transfer
operator|.
name|Representation
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
name|ws
operator|.
name|transfer
operator|.
name|dialect
operator|.
name|fragment
operator|.
name|ExpressionType
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
name|ws
operator|.
name|transfer
operator|.
name|dialect
operator|.
name|fragment
operator|.
name|Fragment
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
name|ws
operator|.
name|transfer
operator|.
name|dialect
operator|.
name|fragment
operator|.
name|FragmentDialectConstants
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
name|ws
operator|.
name|transfer
operator|.
name|dialect
operator|.
name|fragment
operator|.
name|ValueType
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
name|ws
operator|.
name|transfer
operator|.
name|manager
operator|.
name|MemoryResourceManager
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
name|ws
operator|.
name|transfer
operator|.
name|manager
operator|.
name|ResourceManager
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
name|ws
operator|.
name|transfer
operator|.
name|resource
operator|.
name|Resource
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
name|FragmentPutInsertAfterTest
extends|extends
name|IntegrationBaseTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|insertAfter1Test
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|String
name|content
init|=
literal|"<a><b/><c/></a>"
decl_stmt|;
name|ResourceManager
name|resourceManager
init|=
operator|new
name|MemoryResourceManager
argument_list|()
decl_stmt|;
name|ReferenceParametersType
name|refParams
init|=
name|resourceManager
operator|.
name|create
argument_list|(
name|getRepresentation
argument_list|(
name|content
argument_list|)
argument_list|)
decl_stmt|;
name|Server
name|resource
init|=
name|createLocalResource
argument_list|(
name|resourceManager
argument_list|)
decl_stmt|;
name|Resource
name|client
init|=
name|createClient
argument_list|(
name|refParams
argument_list|)
decl_stmt|;
name|Put
name|request
init|=
operator|new
name|Put
argument_list|()
decl_stmt|;
name|request
operator|.
name|setDialect
argument_list|(
name|FragmentDialectConstants
operator|.
name|FRAGMENT_2011_03_IRI
argument_list|)
expr_stmt|;
name|Fragment
name|fragment
init|=
operator|new
name|Fragment
argument_list|()
decl_stmt|;
name|ExpressionType
name|expression
init|=
operator|new
name|ExpressionType
argument_list|()
decl_stmt|;
name|expression
operator|.
name|setLanguage
argument_list|(
name|FragmentDialectConstants
operator|.
name|XPATH10_LANGUAGE_IRI
argument_list|)
expr_stmt|;
name|expression
operator|.
name|setMode
argument_list|(
name|FragmentDialectConstants
operator|.
name|FRAGMENT_MODE_INSERT_AFTER
argument_list|)
expr_stmt|;
name|expression
operator|.
name|getContent
argument_list|()
operator|.
name|add
argument_list|(
literal|"/a/b"
argument_list|)
expr_stmt|;
name|Element
name|addedElement
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"d"
argument_list|)
decl_stmt|;
name|ValueType
name|value
init|=
operator|new
name|ValueType
argument_list|()
decl_stmt|;
name|value
operator|.
name|getContent
argument_list|()
operator|.
name|add
argument_list|(
name|addedElement
argument_list|)
expr_stmt|;
name|fragment
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|fragment
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|fragment
argument_list|)
expr_stmt|;
name|PutResponse
name|response
init|=
name|client
operator|.
name|put
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|Element
name|rootEl
init|=
operator|(
name|Element
operator|)
name|response
operator|.
name|getRepresentation
argument_list|()
operator|.
name|getAny
argument_list|()
decl_stmt|;
name|Element
name|child0
init|=
operator|(
name|Element
operator|)
name|rootEl
operator|.
name|getChildNodes
argument_list|()
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Element
name|child1
init|=
operator|(
name|Element
operator|)
name|rootEl
operator|.
name|getChildNodes
argument_list|()
operator|.
name|item
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Element
name|child2
init|=
operator|(
name|Element
operator|)
name|rootEl
operator|.
name|getChildNodes
argument_list|()
operator|.
name|item
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|rootEl
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|child0
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"d"
argument_list|,
name|child1
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|child2
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
name|resource
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|insertAfter2Test
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|String
name|content
init|=
literal|"<a><b/><b/></a>"
decl_stmt|;
name|ResourceManager
name|resourceManager
init|=
operator|new
name|MemoryResourceManager
argument_list|()
decl_stmt|;
name|ReferenceParametersType
name|refParams
init|=
name|resourceManager
operator|.
name|create
argument_list|(
name|getRepresentation
argument_list|(
name|content
argument_list|)
argument_list|)
decl_stmt|;
name|Server
name|resource
init|=
name|createLocalResource
argument_list|(
name|resourceManager
argument_list|)
decl_stmt|;
name|Resource
name|client
init|=
name|createClient
argument_list|(
name|refParams
argument_list|)
decl_stmt|;
name|Put
name|request
init|=
operator|new
name|Put
argument_list|()
decl_stmt|;
name|request
operator|.
name|setDialect
argument_list|(
name|FragmentDialectConstants
operator|.
name|FRAGMENT_2011_03_IRI
argument_list|)
expr_stmt|;
name|Fragment
name|fragment
init|=
operator|new
name|Fragment
argument_list|()
decl_stmt|;
name|ExpressionType
name|expression
init|=
operator|new
name|ExpressionType
argument_list|()
decl_stmt|;
name|expression
operator|.
name|setLanguage
argument_list|(
name|FragmentDialectConstants
operator|.
name|XPATH10_LANGUAGE_IRI
argument_list|)
expr_stmt|;
name|expression
operator|.
name|setMode
argument_list|(
name|FragmentDialectConstants
operator|.
name|FRAGMENT_MODE_INSERT_AFTER
argument_list|)
expr_stmt|;
name|expression
operator|.
name|getContent
argument_list|()
operator|.
name|add
argument_list|(
literal|"/a/b[last()]"
argument_list|)
expr_stmt|;
name|Element
name|addedElement
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"c"
argument_list|)
decl_stmt|;
name|ValueType
name|value
init|=
operator|new
name|ValueType
argument_list|()
decl_stmt|;
name|value
operator|.
name|getContent
argument_list|()
operator|.
name|add
argument_list|(
name|addedElement
argument_list|)
expr_stmt|;
name|fragment
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|fragment
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|fragment
argument_list|)
expr_stmt|;
name|PutResponse
name|response
init|=
name|client
operator|.
name|put
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|Element
name|rootEl
init|=
operator|(
name|Element
operator|)
name|response
operator|.
name|getRepresentation
argument_list|()
operator|.
name|getAny
argument_list|()
decl_stmt|;
name|Element
name|child0
init|=
operator|(
name|Element
operator|)
name|rootEl
operator|.
name|getChildNodes
argument_list|()
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Element
name|child1
init|=
operator|(
name|Element
operator|)
name|rootEl
operator|.
name|getChildNodes
argument_list|()
operator|.
name|item
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Element
name|child2
init|=
operator|(
name|Element
operator|)
name|rootEl
operator|.
name|getChildNodes
argument_list|()
operator|.
name|item
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|rootEl
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|child0
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|child1
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|child2
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
name|resource
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|insertAfterEmptyDocTest
parameter_list|()
block|{
name|ResourceManager
name|resourceManager
init|=
operator|new
name|MemoryResourceManager
argument_list|()
decl_stmt|;
name|ReferenceParametersType
name|refParams
init|=
name|resourceManager
operator|.
name|create
argument_list|(
operator|new
name|Representation
argument_list|()
argument_list|)
decl_stmt|;
name|Server
name|resource
init|=
name|createLocalResource
argument_list|(
name|resourceManager
argument_list|)
decl_stmt|;
name|Resource
name|client
init|=
name|createClient
argument_list|(
name|refParams
argument_list|)
decl_stmt|;
name|Put
name|request
init|=
operator|new
name|Put
argument_list|()
decl_stmt|;
name|request
operator|.
name|setDialect
argument_list|(
name|FragmentDialectConstants
operator|.
name|FRAGMENT_2011_03_IRI
argument_list|)
expr_stmt|;
name|Fragment
name|fragment
init|=
operator|new
name|Fragment
argument_list|()
decl_stmt|;
name|ExpressionType
name|expression
init|=
operator|new
name|ExpressionType
argument_list|()
decl_stmt|;
name|expression
operator|.
name|setLanguage
argument_list|(
name|FragmentDialectConstants
operator|.
name|XPATH10_LANGUAGE_IRI
argument_list|)
expr_stmt|;
name|expression
operator|.
name|setMode
argument_list|(
name|FragmentDialectConstants
operator|.
name|FRAGMENT_MODE_INSERT_AFTER
argument_list|)
expr_stmt|;
name|expression
operator|.
name|getContent
argument_list|()
operator|.
name|add
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|Element
name|addedElement
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|ValueType
name|value
init|=
operator|new
name|ValueType
argument_list|()
decl_stmt|;
name|value
operator|.
name|getContent
argument_list|()
operator|.
name|add
argument_list|(
name|addedElement
argument_list|)
expr_stmt|;
name|fragment
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|fragment
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|fragment
argument_list|)
expr_stmt|;
name|PutResponse
name|response
init|=
name|client
operator|.
name|put
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|Element
name|rootEl
init|=
operator|(
name|Element
operator|)
name|response
operator|.
name|getRepresentation
argument_list|()
operator|.
name|getAny
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|rootEl
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
name|resource
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|SOAPFaultException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|insertAfterRootTest
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|String
name|content
init|=
literal|"<a/>"
decl_stmt|;
name|ResourceManager
name|resourceManager
init|=
operator|new
name|MemoryResourceManager
argument_list|()
decl_stmt|;
name|ReferenceParametersType
name|refParams
init|=
name|resourceManager
operator|.
name|create
argument_list|(
name|getRepresentation
argument_list|(
name|content
argument_list|)
argument_list|)
decl_stmt|;
name|Server
name|resource
init|=
name|createLocalResource
argument_list|(
name|resourceManager
argument_list|)
decl_stmt|;
name|Resource
name|client
init|=
name|createClient
argument_list|(
name|refParams
argument_list|)
decl_stmt|;
name|Put
name|request
init|=
operator|new
name|Put
argument_list|()
decl_stmt|;
name|request
operator|.
name|setDialect
argument_list|(
name|FragmentDialectConstants
operator|.
name|FRAGMENT_2011_03_IRI
argument_list|)
expr_stmt|;
name|Fragment
name|fragment
init|=
operator|new
name|Fragment
argument_list|()
decl_stmt|;
name|ExpressionType
name|expression
init|=
operator|new
name|ExpressionType
argument_list|()
decl_stmt|;
name|expression
operator|.
name|setLanguage
argument_list|(
name|FragmentDialectConstants
operator|.
name|XPATH10_LANGUAGE_IRI
argument_list|)
expr_stmt|;
name|expression
operator|.
name|setMode
argument_list|(
name|FragmentDialectConstants
operator|.
name|FRAGMENT_MODE_INSERT_AFTER
argument_list|)
expr_stmt|;
name|expression
operator|.
name|getContent
argument_list|()
operator|.
name|add
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|Element
name|addedElement
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"b"
argument_list|)
decl_stmt|;
name|ValueType
name|value
init|=
operator|new
name|ValueType
argument_list|()
decl_stmt|;
name|value
operator|.
name|getContent
argument_list|()
operator|.
name|add
argument_list|(
name|addedElement
argument_list|)
expr_stmt|;
name|fragment
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|fragment
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|fragment
argument_list|)
expr_stmt|;
name|client
operator|.
name|put
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|resource
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|SOAPFaultException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|insertAfterAttrTest
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|String
name|content
init|=
literal|"<a foo=\"1\"/>"
decl_stmt|;
name|ResourceManager
name|resourceManager
init|=
operator|new
name|MemoryResourceManager
argument_list|()
decl_stmt|;
name|ReferenceParametersType
name|refParams
init|=
name|resourceManager
operator|.
name|create
argument_list|(
name|getRepresentation
argument_list|(
name|content
argument_list|)
argument_list|)
decl_stmt|;
name|Server
name|resource
init|=
name|createLocalResource
argument_list|(
name|resourceManager
argument_list|)
decl_stmt|;
name|Resource
name|client
init|=
name|createClient
argument_list|(
name|refParams
argument_list|)
decl_stmt|;
name|Put
name|request
init|=
operator|new
name|Put
argument_list|()
decl_stmt|;
name|request
operator|.
name|setDialect
argument_list|(
name|FragmentDialectConstants
operator|.
name|FRAGMENT_2011_03_IRI
argument_list|)
expr_stmt|;
name|Fragment
name|fragment
init|=
operator|new
name|Fragment
argument_list|()
decl_stmt|;
name|ExpressionType
name|expression
init|=
operator|new
name|ExpressionType
argument_list|()
decl_stmt|;
name|expression
operator|.
name|setLanguage
argument_list|(
name|FragmentDialectConstants
operator|.
name|XPATH10_LANGUAGE_IRI
argument_list|)
expr_stmt|;
name|expression
operator|.
name|setMode
argument_list|(
name|FragmentDialectConstants
operator|.
name|FRAGMENT_MODE_INSERT_AFTER
argument_list|)
expr_stmt|;
name|expression
operator|.
name|getContent
argument_list|()
operator|.
name|add
argument_list|(
literal|"/a/@foo"
argument_list|)
expr_stmt|;
name|Element
name|addedElement
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"b"
argument_list|)
decl_stmt|;
name|ValueType
name|value
init|=
operator|new
name|ValueType
argument_list|()
decl_stmt|;
name|value
operator|.
name|getContent
argument_list|()
operator|.
name|add
argument_list|(
name|addedElement
argument_list|)
expr_stmt|;
name|fragment
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|fragment
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|fragment
argument_list|)
expr_stmt|;
name|client
operator|.
name|put
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|resource
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

