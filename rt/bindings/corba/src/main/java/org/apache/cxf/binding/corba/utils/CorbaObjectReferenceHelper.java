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
name|binding
operator|.
name|corba
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Binding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Import
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Port
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverter
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
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|BindingType
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
name|logging
operator|.
name|LogUtils
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
name|CastUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Object
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|CorbaObjectReferenceHelper
block|{
specifier|public
specifier|static
specifier|final
name|String
name|WSDLI_NAMESPACE_URI
init|=
literal|"http://www.w3.org/2006/01/wsdl-instance"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ADDRESSING_NAMESPACE_URI
init|=
literal|"http://www.w3.org/2005/08/addressing"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ADDRESSING_WSDL_NAMESPACE_URI
init|=
literal|"http://www.w3.org/2006/05/addressing/wsdl"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|CorbaObjectReferenceHelper
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|CorbaObjectReferenceHelper
parameter_list|()
block|{
comment|//utility class
block|}
specifier|public
specifier|static
name|String
name|getWSDLLocation
parameter_list|(
name|Definition
name|wsdlDef
parameter_list|)
block|{
return|return
name|wsdlDef
operator|.
name|getDocumentBaseURI
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|QName
name|getServiceName
parameter_list|(
name|Binding
name|binding
parameter_list|,
name|Definition
name|wsdlDef
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Getting service name for an object reference"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Service
argument_list|>
name|services
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|wsdlDef
operator|.
name|getServices
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Service
name|serv
range|:
name|services
control|)
block|{
name|Collection
argument_list|<
name|Port
argument_list|>
name|ports
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|serv
operator|.
name|getPorts
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Port
name|pt
range|:
name|ports
control|)
block|{
if|if
condition|(
name|pt
operator|.
name|getBinding
argument_list|()
operator|.
name|equals
argument_list|(
name|binding
argument_list|)
condition|)
block|{
return|return
name|serv
operator|.
name|getQName
argument_list|()
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|getEndpointName
parameter_list|(
name|Binding
name|binding
parameter_list|,
name|Definition
name|wsdlDef
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Getting endpoint name for an object reference"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Service
argument_list|>
name|services
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|wsdlDef
operator|.
name|getServices
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Service
name|serv
range|:
name|services
control|)
block|{
name|Collection
argument_list|<
name|Port
argument_list|>
name|ports
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|serv
operator|.
name|getPorts
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Port
name|pt
range|:
name|ports
control|)
block|{
if|if
condition|(
name|pt
operator|.
name|getBinding
argument_list|()
operator|.
name|equals
argument_list|(
name|binding
argument_list|)
condition|)
block|{
return|return
name|pt
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|Binding
name|getDefaultBinding
parameter_list|(
name|Object
name|obj
parameter_list|,
name|Definition
name|wsdlDef
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|,
literal|"Getting binding for a default object reference"
argument_list|)
expr_stmt|;
name|Collection
name|bindings
init|=
name|wsdlDef
operator|.
name|getBindings
argument_list|()
operator|.
name|values
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|bindings
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Binding
name|b
init|=
operator|(
name|Binding
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|List
name|extElements
init|=
name|b
operator|.
name|getExtensibilityElements
argument_list|()
decl_stmt|;
comment|// Get the list of all extensibility elements
for|for
control|(
name|Iterator
name|extIter
init|=
name|extElements
operator|.
name|iterator
argument_list|()
init|;
name|extIter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|java
operator|.
name|lang
operator|.
name|Object
name|element
init|=
name|extIter
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// Find a binding type so we can check against its repository ID
if|if
condition|(
name|element
operator|instanceof
name|BindingType
condition|)
block|{
name|BindingType
name|type
init|=
operator|(
name|BindingType
operator|)
name|element
decl_stmt|;
if|if
condition|(
name|obj
operator|.
name|_is_a
argument_list|(
name|type
operator|.
name|getRepositoryID
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|b
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|EprMetaData
name|getBindingForTypeId
parameter_list|(
name|String
name|repId
parameter_list|,
name|Definition
name|wsdlDef
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"RepositoryId "
operator|+
name|repId
operator|+
literal|", wsdl namespace "
operator|+
name|wsdlDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|EprMetaData
name|ret
init|=
operator|new
name|EprMetaData
argument_list|()
decl_stmt|;
name|Collection
name|bindings
init|=
name|wsdlDef
operator|.
name|getBindings
argument_list|()
operator|.
name|values
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|bindings
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Binding
name|b
init|=
operator|(
name|Binding
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|List
name|extElements
init|=
name|b
operator|.
name|getExtensibilityElements
argument_list|()
decl_stmt|;
comment|// Get the list of all extensibility elements
for|for
control|(
name|Iterator
name|extIter
init|=
name|extElements
operator|.
name|iterator
argument_list|()
init|;
name|extIter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|java
operator|.
name|lang
operator|.
name|Object
name|element
init|=
name|extIter
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// Find a binding type so we can check against its repository ID
if|if
condition|(
name|element
operator|instanceof
name|BindingType
condition|)
block|{
name|BindingType
name|type
init|=
operator|(
name|BindingType
operator|)
name|element
decl_stmt|;
if|if
condition|(
name|repId
operator|.
name|equals
argument_list|(
name|type
operator|.
name|getRepositoryID
argument_list|()
argument_list|)
condition|)
block|{
name|ret
operator|.
name|setCandidateWsdlDef
argument_list|(
name|wsdlDef
argument_list|)
expr_stmt|;
name|ret
operator|.
name|setBinding
argument_list|(
name|b
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|ret
operator|.
name|isValid
argument_list|()
condition|)
block|{
comment|// recursivly check imports
name|Iterator
name|importLists
init|=
name|wsdlDef
operator|.
name|getImports
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|importLists
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|List
name|imports
init|=
operator|(
name|List
operator|)
name|importLists
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|java
operator|.
name|lang
operator|.
name|Object
name|imp
range|:
name|imports
control|)
block|{
if|if
condition|(
name|imp
operator|instanceof
name|Import
condition|)
block|{
name|Definition
name|importDef
init|=
operator|(
operator|(
name|Import
operator|)
name|imp
operator|)
operator|.
name|getDefinition
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"Following import "
operator|+
name|importDef
operator|.
name|getDocumentBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|ret
operator|=
name|getBindingForTypeId
argument_list|(
name|repId
argument_list|,
name|importDef
argument_list|)
expr_stmt|;
if|if
condition|(
name|ret
operator|.
name|isValid
argument_list|()
condition|)
block|{
return|return
name|ret
return|;
block|}
block|}
block|}
block|}
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|String
name|extractTypeIdFromIOR
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|String
name|ret
init|=
operator|new
name|String
argument_list|()
decl_stmt|;
name|byte
name|data
index|[]
init|=
name|DatatypeConverter
operator|.
name|parseHexBinary
argument_list|(
name|url
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|.
name|length
operator|>
literal|0
condition|)
block|{
comment|// parse out type_id from IOR CDR encapsulation
name|boolean
name|bigIndian
init|=
operator|!
operator|(
name|data
index|[
literal|0
index|]
operator|>
literal|0
operator|)
decl_stmt|;
name|int
name|typeIdStringSize
init|=
name|readIntFromAlignedCDREncaps
argument_list|(
name|data
argument_list|,
literal|4
argument_list|,
name|bigIndian
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeIdStringSize
operator|>
literal|1
condition|)
block|{
name|ret
operator|=
name|readStringFromAlignedCDREncaps
argument_list|(
name|data
argument_list|,
literal|8
argument_list|,
name|typeIdStringSize
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
specifier|private
specifier|static
name|String
name|readStringFromAlignedCDREncaps
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|startIndex
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|char
index|[]
name|arr
init|=
operator|new
name|char
index|[
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|length
condition|;
name|i
operator|++
control|)
block|{
name|arr
index|[
name|i
index|]
operator|=
call|(
name|char
call|)
argument_list|(
name|data
index|[
name|startIndex
operator|+
name|i
index|]
operator|&
literal|0xff
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|String
argument_list|(
name|arr
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|int
name|readIntFromAlignedCDREncaps
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|index
parameter_list|,
name|boolean
name|bigEndian
parameter_list|)
block|{
if|if
condition|(
name|bigEndian
condition|)
block|{
name|int
name|partial
init|=
operator|(
operator|(
operator|(
operator|(
name|int
operator|)
name|data
index|[
name|index
index|]
operator|)
operator|<<
literal|24
operator|)
operator|&
literal|0xff000000
operator|)
operator||
operator|(
operator|(
operator|(
operator|(
name|int
operator|)
name|data
index|[
name|index
operator|+
literal|1
index|]
operator|)
operator|<<
literal|16
operator|)
operator|&
literal|0x00ff0000
operator|)
decl_stmt|;
return|return
name|partial
operator||
operator|(
operator|(
operator|(
operator|(
name|int
operator|)
name|data
index|[
name|index
operator|+
literal|2
index|]
operator|)
operator|<<
literal|8
operator|)
operator|&
literal|0x0000ff00
operator|)
operator||
operator|(
operator|(
operator|(
operator|(
name|int
operator|)
name|data
index|[
name|index
operator|+
literal|3
index|]
operator|)
operator|)
operator|&
literal|0x000000ff
operator|)
return|;
block|}
else|else
block|{
name|int
name|partial
init|=
operator|(
operator|(
operator|(
operator|(
name|int
operator|)
name|data
index|[
name|index
index|]
operator|)
operator|)
operator|&
literal|0x000000ff
operator|)
operator||
operator|(
operator|(
operator|(
operator|(
name|int
operator|)
name|data
index|[
name|index
operator|+
literal|1
index|]
operator|)
operator|<<
literal|8
operator|)
operator|&
literal|0x0000ff00
operator|)
decl_stmt|;
return|return
name|partial
operator||
operator|(
operator|(
operator|(
operator|(
name|int
operator|)
name|data
index|[
name|index
operator|+
literal|2
index|]
operator|)
operator|<<
literal|16
operator|)
operator|&
literal|0x00ff0000
operator|)
operator||
operator|(
operator|(
operator|(
operator|(
name|int
operator|)
name|data
index|[
name|index
operator|+
literal|3
index|]
operator|)
operator|<<
literal|24
operator|)
operator|&
literal|0xff000000
operator|)
return|;
block|}
block|}
specifier|public
specifier|static
name|void
name|populateEprInfo
parameter_list|(
name|EprMetaData
name|info
parameter_list|)
block|{
if|if
condition|(
operator|!
name|info
operator|.
name|isValid
argument_list|()
condition|)
block|{
return|return;
block|}
name|Binding
name|match
init|=
name|info
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|Definition
name|wsdlDef
init|=
name|info
operator|.
name|getCandidateWsdlDef
argument_list|()
decl_stmt|;
name|Collection
name|services
init|=
name|wsdlDef
operator|.
name|getServices
argument_list|()
operator|.
name|values
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|services
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Service
name|serv
init|=
operator|(
name|Service
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Collection
name|ports
init|=
name|serv
operator|.
name|getPorts
argument_list|()
operator|.
name|values
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|portIter
init|=
name|ports
operator|.
name|iterator
argument_list|()
init|;
name|portIter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Port
name|pt
init|=
operator|(
name|Port
operator|)
name|portIter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pt
operator|.
name|getBinding
argument_list|()
operator|.
name|equals
argument_list|(
name|match
argument_list|)
condition|)
block|{
name|info
operator|.
name|setPortName
argument_list|(
name|pt
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|setServiceQName
argument_list|(
name|serv
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|info
operator|.
name|getServiceQName
argument_list|()
operator|==
literal|null
condition|)
block|{
name|Iterator
name|importLists
init|=
name|wsdlDef
operator|.
name|getImports
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|info
operator|.
name|getServiceQName
argument_list|()
operator|==
literal|null
operator|&&
name|importLists
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|List
name|imports
init|=
operator|(
name|List
operator|)
name|importLists
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|java
operator|.
name|lang
operator|.
name|Object
name|imp
range|:
name|imports
control|)
block|{
if|if
condition|(
name|imp
operator|instanceof
name|Import
condition|)
block|{
name|Definition
name|importDef
init|=
operator|(
operator|(
name|Import
operator|)
name|imp
operator|)
operator|.
name|getDefinition
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"following wsdl import "
operator|+
name|importDef
operator|.
name|getDocumentBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|setCandidateWsdlDef
argument_list|(
name|importDef
argument_list|)
expr_stmt|;
name|populateEprInfo
argument_list|(
name|info
argument_list|)
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|getServiceQName
argument_list|()
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

