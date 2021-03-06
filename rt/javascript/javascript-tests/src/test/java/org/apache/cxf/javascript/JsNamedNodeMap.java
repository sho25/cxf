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
name|javascript
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
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
name|NamedNodeMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|ContextFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|Scriptable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|ScriptableObject
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|JsNamedNodeMap
extends|extends
name|ScriptableObject
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|3714155882606691342L
decl_stmt|;
specifier|private
name|NamedNodeMap
name|wrappedMap
decl_stmt|;
specifier|public
name|JsNamedNodeMap
parameter_list|()
block|{
comment|// just to make Rhino happy.
block|}
annotation|@
name|Override
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
literal|"NamedNodeMap"
return|;
block|}
specifier|public
specifier|static
name|void
name|register
parameter_list|(
name|ScriptableObject
name|scope
parameter_list|)
block|{
try|try
block|{
name|ScriptableObject
operator|.
name|defineClass
argument_list|(
name|scope
argument_list|,
name|JsNamedNodeMap
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
decl||
name|InstantiationException
decl||
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/** * @return Returns the wrappedMap.      */
specifier|public
name|NamedNodeMap
name|getWrappedMap
parameter_list|()
block|{
return|return
name|wrappedMap
return|;
block|}
comment|/**      * @param wrappedMap The wrappedMap to set.      */
specifier|public
name|void
name|setWrappedMap
parameter_list|(
name|NamedNodeMap
name|wrappedMap
parameter_list|)
block|{
name|this
operator|.
name|wrappedMap
operator|=
name|wrappedMap
expr_stmt|;
block|}
comment|// Rhino won't let us use a constructor.
name|void
name|initialize
parameter_list|(
name|NamedNodeMap
name|map
parameter_list|)
block|{
name|wrappedMap
operator|=
name|map
expr_stmt|;
block|}
specifier|public
specifier|static
name|JsNamedNodeMap
name|wrapMap
parameter_list|(
name|Scriptable
name|scope
parameter_list|,
name|NamedNodeMap
name|map
parameter_list|)
block|{
name|Context
name|cx
init|=
name|ContextFactory
operator|.
name|getGlobal
argument_list|()
operator|.
name|enterContext
argument_list|()
decl_stmt|;
name|JsNamedNodeMap
name|newObject
init|=
operator|(
name|JsNamedNodeMap
operator|)
name|cx
operator|.
name|newObject
argument_list|(
name|scope
argument_list|,
literal|"NamedNodeMap"
argument_list|)
decl_stmt|;
name|newObject
operator|.
name|initialize
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|newObject
return|;
block|}
comment|// CHECKSTYLE:OFF
specifier|public
name|int
name|jsGet_length
parameter_list|()
block|{
return|return
name|wrappedMap
operator|.
name|getLength
argument_list|()
return|;
block|}
specifier|public
name|Object
name|jsFunction_getNamedItem
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|JsSimpleDomNode
operator|.
name|wrapNode
argument_list|(
name|getParentScope
argument_list|()
argument_list|,
name|wrappedMap
operator|.
name|getNamedItem
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Object
name|jsFunction_getNamedItemNS
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|local
parameter_list|)
block|{
return|return
name|JsSimpleDomNode
operator|.
name|wrapNode
argument_list|(
name|getParentScope
argument_list|()
argument_list|,
name|wrappedMap
operator|.
name|getNamedItemNS
argument_list|(
name|uri
argument_list|,
name|local
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Object
name|jsFunction_item
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|JsSimpleDomNode
operator|.
name|wrapNode
argument_list|(
name|getParentScope
argument_list|()
argument_list|,
name|wrappedMap
operator|.
name|item
argument_list|(
name|index
argument_list|)
argument_list|)
return|;
block|}
comment|// don't implement the 'modify' APIs.
block|}
end_class

end_unit

