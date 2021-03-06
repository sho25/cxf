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
name|aegis
operator|.
name|type
operator|.
name|encoded
package|;
end_package

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
name|TreeMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|Attr
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|aegis
operator|.
name|AegisContext
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
name|aegis
operator|.
name|Context
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
name|aegis
operator|.
name|type
operator|.
name|basic
operator|.
name|BeanTypeInfo
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
name|aegis
operator|.
name|xml
operator|.
name|stax
operator|.
name|ElementReader
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
name|junit
operator|.
name|Before
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotSame
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_class
specifier|public
class|class
name|StructTypeTest
extends|extends
name|AbstractEncodedTest
block|{
specifier|private
name|StructType
name|addressType
decl_stmt|;
specifier|private
name|StructType
name|purchaseOrderType
decl_stmt|;
specifier|private
name|Context
name|getLocalContext
parameter_list|()
block|{
name|AegisContext
name|aegisContext
init|=
operator|new
name|AegisContext
argument_list|()
decl_stmt|;
return|return
operator|new
name|Context
argument_list|(
name|aegisContext
argument_list|)
return|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|// address type
name|BeanTypeInfo
name|addressInfo
init|=
operator|new
name|BeanTypeInfo
argument_list|(
name|Address
operator|.
name|class
argument_list|,
literal|"urn:Bean"
argument_list|)
decl_stmt|;
name|addressInfo
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|addressType
operator|=
operator|new
name|StructType
argument_list|(
name|addressInfo
argument_list|)
expr_stmt|;
name|addressType
operator|.
name|setTypeClass
argument_list|(
name|Address
operator|.
name|class
argument_list|)
expr_stmt|;
name|addressType
operator|.
name|setSchemaType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:Bean"
argument_list|,
literal|"address"
argument_list|)
argument_list|)
expr_stmt|;
name|mapping
operator|.
name|register
argument_list|(
name|addressType
argument_list|)
expr_stmt|;
comment|// purchase order type
name|BeanTypeInfo
name|poInfo
init|=
operator|new
name|BeanTypeInfo
argument_list|(
name|PurchaseOrder
operator|.
name|class
argument_list|,
literal|"urn:Bean"
argument_list|)
decl_stmt|;
name|poInfo
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|purchaseOrderType
operator|=
operator|new
name|StructType
argument_list|(
name|poInfo
argument_list|)
expr_stmt|;
name|purchaseOrderType
operator|.
name|setTypeClass
argument_list|(
name|PurchaseOrder
operator|.
name|class
argument_list|)
expr_stmt|;
name|purchaseOrderType
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|purchaseOrderType
operator|.
name|setSchemaType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:Bean"
argument_list|,
literal|"po"
argument_list|)
argument_list|)
expr_stmt|;
name|mapping
operator|.
name|register
argument_list|(
name|purchaseOrderType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleStruct
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Test reading
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"struct1.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Address
name|address
init|=
operator|(
name|Address
operator|)
name|addressType
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|getLocalContext
argument_list|()
argument_list|)
decl_stmt|;
name|validateShippingAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Test reading - no namespace on nested elements
name|reader
operator|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"struct2.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|address
operator|=
operator|(
name|Address
operator|)
name|addressType
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|getLocalContext
argument_list|()
argument_list|)
expr_stmt|;
name|validateShippingAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Test writing
name|Element
name|element
init|=
name|writeObjectToElement
argument_list|(
name|addressType
argument_list|,
name|address
argument_list|,
name|getLocalContext
argument_list|()
argument_list|)
decl_stmt|;
name|validateShippingAddress
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testComplexStruct
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Test reading
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"struct3.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|PurchaseOrder
name|po
init|=
operator|(
name|PurchaseOrder
operator|)
name|purchaseOrderType
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|getLocalContext
argument_list|()
argument_list|)
decl_stmt|;
name|validatePurchaseOrder
argument_list|(
name|po
argument_list|)
expr_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Test reading - no namespace on nested elements
name|reader
operator|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"struct4.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|po
operator|=
operator|(
name|PurchaseOrder
operator|)
name|purchaseOrderType
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|getLocalContext
argument_list|()
argument_list|)
expr_stmt|;
name|validatePurchaseOrder
argument_list|(
name|po
argument_list|)
expr_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Test writing
name|Element
name|element
init|=
name|writeRef
argument_list|(
name|po
argument_list|)
decl_stmt|;
name|validatePurchaseOrder
argument_list|(
name|element
argument_list|)
expr_stmt|;
comment|// Test reading - no namespace on nested elements, xsi:nil (CXF-2695)
name|reader
operator|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"struct5.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|po
operator|=
operator|(
name|PurchaseOrder
operator|)
name|purchaseOrderType
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|getLocalContext
argument_list|()
argument_list|)
expr_stmt|;
name|validatePurchaseOrder
argument_list|(
name|po
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStructRef
parameter_list|()
throws|throws
name|Exception
block|{
name|PurchaseOrder
name|purchaseOrder
decl_stmt|;
comment|// Simple nested ref
name|purchaseOrder
operator|=
operator|(
name|PurchaseOrder
operator|)
name|readRef
argument_list|(
literal|"ref1.xml"
argument_list|)
expr_stmt|;
name|validatePurchaseOrder
argument_list|(
name|purchaseOrder
argument_list|)
expr_stmt|;
comment|// Strings referenced
name|purchaseOrder
operator|=
operator|(
name|PurchaseOrder
operator|)
name|readRef
argument_list|(
literal|"ref2.xml"
argument_list|)
expr_stmt|;
name|validatePurchaseOrder
argument_list|(
name|purchaseOrder
argument_list|)
expr_stmt|;
comment|// completely unrolled
name|purchaseOrder
operator|=
operator|(
name|PurchaseOrder
operator|)
name|readRef
argument_list|(
literal|"ref3.xml"
argument_list|)
expr_stmt|;
name|validatePurchaseOrder
argument_list|(
name|purchaseOrder
argument_list|)
expr_stmt|;
comment|// Test writing
name|Element
name|element
init|=
name|writeRef
argument_list|(
name|purchaseOrder
argument_list|)
decl_stmt|;
name|validatePurchaseOrder
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|validateShippingAddress
parameter_list|(
name|Element
name|shipping
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"shipping is null"
argument_list|,
name|shipping
argument_list|)
expr_stmt|;
name|assertChildEquals
argument_list|(
literal|"1234 Riverside Drive"
argument_list|,
name|shipping
argument_list|,
literal|"street"
argument_list|)
expr_stmt|;
name|assertChildEquals
argument_list|(
literal|"Gainesville"
argument_list|,
name|shipping
argument_list|,
literal|"city"
argument_list|)
expr_stmt|;
name|assertChildEquals
argument_list|(
literal|"FL"
argument_list|,
name|shipping
argument_list|,
literal|"state"
argument_list|)
expr_stmt|;
name|assertChildEquals
argument_list|(
literal|"30506"
argument_list|,
name|shipping
argument_list|,
literal|"zip"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|validateBillingAddress
parameter_list|(
name|Element
name|billing
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"billing is null"
argument_list|,
name|billing
argument_list|)
expr_stmt|;
name|assertChildEquals
argument_list|(
literal|"1234 Fake Street"
argument_list|,
name|billing
argument_list|,
literal|"street"
argument_list|)
expr_stmt|;
name|assertChildEquals
argument_list|(
literal|"Las Vegas"
argument_list|,
name|billing
argument_list|,
literal|"city"
argument_list|)
expr_stmt|;
name|assertChildEquals
argument_list|(
literal|"NV"
argument_list|,
name|billing
argument_list|,
literal|"state"
argument_list|)
expr_stmt|;
name|assertChildEquals
argument_list|(
literal|"89102"
argument_list|,
name|billing
argument_list|,
literal|"zip"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validatePurchaseOrder
parameter_list|(
name|PurchaseOrder
name|purchaseOrder
parameter_list|)
block|{
name|validatePurchaseOrder
argument_list|(
name|purchaseOrder
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validatePurchaseOrder
parameter_list|(
name|PurchaseOrder
name|purchaseOrder
parameter_list|,
name|boolean
name|nilZip
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|purchaseOrder
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|purchaseOrder
operator|.
name|getShipping
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1234 Riverside Drive"
argument_list|,
name|purchaseOrder
operator|.
name|getShipping
argument_list|()
operator|.
name|getStreet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Gainesville"
argument_list|,
name|purchaseOrder
operator|.
name|getShipping
argument_list|()
operator|.
name|getCity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FL"
argument_list|,
name|purchaseOrder
operator|.
name|getShipping
argument_list|()
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|nilZip
condition|)
block|{
name|assertNull
argument_list|(
name|purchaseOrder
operator|.
name|getShipping
argument_list|()
operator|.
name|getZip
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"30506"
argument_list|,
name|purchaseOrder
operator|.
name|getShipping
argument_list|()
operator|.
name|getZip
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|purchaseOrder
operator|.
name|getBilling
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1234 Fake Street"
argument_list|,
name|purchaseOrder
operator|.
name|getBilling
argument_list|()
operator|.
name|getStreet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Las Vegas"
argument_list|,
name|purchaseOrder
operator|.
name|getBilling
argument_list|()
operator|.
name|getCity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"NV"
argument_list|,
name|purchaseOrder
operator|.
name|getBilling
argument_list|()
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"89102"
argument_list|,
name|purchaseOrder
operator|.
name|getBilling
argument_list|()
operator|.
name|getZip
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validatePurchaseOrder
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|Exception
block|{
name|Element
name|poRefElement
init|=
literal|null
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Element
argument_list|>
name|blocks
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Node
name|n
init|=
name|element
operator|.
name|getFirstChild
argument_list|()
init|;
name|n
operator|!=
literal|null
condition|;
name|n
operator|=
name|n
operator|.
name|getNextSibling
argument_list|()
control|)
block|{
if|if
condition|(
name|n
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|child
init|=
operator|(
name|Element
operator|)
name|n
decl_stmt|;
if|if
condition|(
name|poRefElement
operator|==
literal|null
condition|)
block|{
name|poRefElement
operator|=
name|child
expr_stmt|;
block|}
else|else
block|{
name|String
name|id
init|=
name|getId
argument_list|(
literal|"Trailing block "
argument_list|,
name|child
argument_list|)
decl_stmt|;
name|blocks
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Element
name|po
init|=
name|getReferencedElement
argument_list|(
literal|"poRef"
argument_list|,
name|poRefElement
argument_list|,
name|blocks
argument_list|)
decl_stmt|;
name|Element
name|shippingRef
init|=
operator|(
name|Element
operator|)
name|DOMUtils
operator|.
name|getChild
argument_list|(
name|po
argument_list|,
literal|"shipping"
argument_list|)
decl_stmt|;
name|Element
name|shipping
init|=
name|getReferencedElement
argument_list|(
literal|"shipping"
argument_list|,
name|shippingRef
argument_list|,
name|blocks
argument_list|)
decl_stmt|;
name|validateShippingAddress
argument_list|(
name|shipping
argument_list|)
expr_stmt|;
name|Element
name|billingRef
init|=
operator|(
name|Element
operator|)
name|DOMUtils
operator|.
name|getChild
argument_list|(
name|po
argument_list|,
literal|"billing"
argument_list|)
decl_stmt|;
name|Element
name|billing
init|=
name|getReferencedElement
argument_list|(
literal|"billing"
argument_list|,
name|billingRef
argument_list|,
name|blocks
argument_list|)
decl_stmt|;
name|validateBillingAddress
argument_list|(
name|billing
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Element
name|getReferencedElement
parameter_list|(
name|String
name|childName
parameter_list|,
name|Element
name|element
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Element
argument_list|>
name|blocks
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|childName
operator|+
literal|" is null"
argument_list|,
name|element
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"element is null"
argument_list|,
name|element
argument_list|)
expr_stmt|;
name|String
name|refId
init|=
name|getRef
argument_list|(
name|childName
argument_list|,
name|element
argument_list|)
decl_stmt|;
name|Element
name|refElement
init|=
name|blocks
operator|.
name|get
argument_list|(
name|refId
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|childName
operator|+
literal|" referenced non-existant element "
operator|+
name|refId
argument_list|,
name|refElement
argument_list|)
expr_stmt|;
return|return
name|refElement
return|;
block|}
specifier|private
specifier|static
name|void
name|assertChildEquals
parameter_list|(
name|String
name|expected
parameter_list|,
name|Element
name|element
parameter_list|,
name|String
name|childName
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|DOMUtils
operator|.
name|getChild
argument_list|(
name|element
argument_list|,
name|childName
argument_list|)
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getId
parameter_list|(
name|String
name|childName
parameter_list|,
name|Element
name|child
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|childName
operator|+
literal|" is null"
argument_list|,
name|child
argument_list|)
expr_stmt|;
name|Attr
name|idAttribute
init|=
name|child
operator|.
name|getAttributeNode
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|childName
operator|+
literal|" id is null \n"
argument_list|,
name|idAttribute
argument_list|)
expr_stmt|;
name|String
name|id
init|=
name|idAttribute
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|childName
operator|+
literal|" id is null \n"
argument_list|,
name|id
argument_list|)
expr_stmt|;
return|return
name|id
return|;
block|}
specifier|private
name|String
name|getRef
parameter_list|(
name|String
name|childName
parameter_list|,
name|Element
name|child
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|childName
operator|+
literal|" is null"
argument_list|,
name|child
argument_list|)
expr_stmt|;
name|String
name|hrefAttribute
init|=
name|child
operator|.
name|getAttribute
argument_list|(
literal|"href"
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
literal|""
argument_list|,
name|childName
operator|+
literal|" href is null \n"
argument_list|,
name|hrefAttribute
argument_list|)
expr_stmt|;
return|return
name|hrefAttribute
return|;
block|}
block|}
end_class

end_unit

