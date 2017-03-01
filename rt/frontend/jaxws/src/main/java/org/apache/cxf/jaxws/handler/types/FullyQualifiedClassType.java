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
name|jaxws
operator|.
name|handler
operator|.
name|types
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
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
name|annotation
operator|.
name|XmlAccessorType
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
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_comment
comment|/**  * The elements that use this type designate the name of a Java class or interface. The name is in the form of  * a "binary name", as defined in the JLS. This is the form of name used in Class.forName(). Tools that need  * the canonical name (the name used in source code) will need to convert this binary name to the canonical  * name.  *<p>  * Java class for fully-qualified-classType complex type.  *<p>  * The following schema fragment specifies the expected content contained within this class.  *  *<pre>  *&lt;complexType name="fully-qualified-classType">  *&lt;simpleContent>  *&lt;restriction base="&lt;http://java.sun.com/xml/ns/javaee>string">  *&lt;/restriction>  *&lt;/simpleContent>  *&lt;/complexType>  *</pre>  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"fully-qualified-classType"
argument_list|)
specifier|public
class|class
name|FullyQualifiedClassType
extends|extends
name|CString
block|{  }
end_class

end_unit

